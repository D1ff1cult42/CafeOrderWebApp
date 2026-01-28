package org.example.paymentservice.service.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.common.grpc.order.GetOrderPriceResponse;
import org.example.paymentservice.client.YookassaFeignClient;
import org.example.paymentservice.dto.request.PaymentCreateRequest;
import org.example.paymentservice.dto.request.RefundRequest;
import org.example.paymentservice.dto.response.PaymentResponse;
import org.example.paymentservice.entity.Payment;
import org.example.common.kafka.Status;
import org.example.paymentservice.exceptions.PaymentFailedException;
import org.example.paymentservice.exceptions.PaymentNotFoundException;
import org.example.paymentservice.exceptions.RefundFailedException;
import org.example.paymentservice.grpc.OrderGrpcClient;
import org.example.common.kafka.model.PaymentEvent;
import org.example.paymentservice.kafka.producer.KafkaPaymentEventProducer;
import org.example.paymentservice.mapper.response.PaymentResponseMapper;
import org.example.paymentservice.repository.PaymentRepository;
import org.example.paymentservice.service.interfaces.PaymentService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@Service
@Slf4j
@RequiredArgsConstructor
public class PaymentServiceImpl implements PaymentService {
    private final PaymentRepository paymentRepository;
    private final YookassaFeignClient yookassaFeignClient;
    private final PaymentResponseMapper paymentResponseMapper;
    private final ObjectMapper objectMapper;
    private final OrderGrpcClient orderGrpcClient;
    private final KafkaPaymentEventProducer kafkaProducer;


    @Override
    public PaymentResponse createPayment(PaymentCreateRequest request) {
        log.info("Creating Payment for orderId: {}", request.orderId());

        if(paymentRepository.existsByOrderIdAndStatus(request.orderId(), Status.SUCCEEDED)){
            throw new PaymentFailedException("Payment already exists for orderId: " + request.orderId());
        }

        GetOrderPriceResponse orderPriceResponse = orderGrpcClient.getOrderPrice(request.orderId());
        BigDecimal amount = new BigDecimal(orderPriceResponse.getTotalPrice());

        Payment payment = new Payment();
        payment.setAmount(amount);
        payment.setOrderId(request.orderId());
        payment.setStatus(Status.PENDING);
        payment.setIdempotenceKey(UUID.randomUUID().toString());

        try{
            Map<String, Object> yookassaRequest = buildYookassaPaymentRequest(amount, request);

            log.info("Sending payment request to YooKassa with idempotence key: {}", payment.getIdempotenceKey());

            Map<String, Object> yookassaResponse = yookassaFeignClient.createPayment(
                    payment.getIdempotenceKey(),
                    yookassaRequest
            );

            if(yookassaResponse != null){
                String yookassaId = (String) yookassaResponse.get("id");
                String yookassaStatus = (String) yookassaResponse.get("status");

                log.info("Yookassa back: id={}, status={}", yookassaId, yookassaStatus);

                payment.setYookassaPaymentId(yookassaId);
                payment.setStatus(Status.valueOf(yookassaStatus.toUpperCase()));

                Map<String, Object> confirmation = (Map<String, Object>) yookassaResponse.get("confirmation");
                if(confirmation != null){
                    String confirmationUrl = (String) confirmation.get("confirmation_url");
                    payment.setYookassaConfirmationUrl(confirmationUrl);
                    log.info("Confirmation URL: {}", confirmationUrl);
                }
            }
        } catch (Exception e) {
            log.error("Failed to create payment in Yookassa: {}", e.getMessage(), e);
            payment.setStatus(Status.CANCELED);
            payment.setMetadata("Error: " + e.getMessage());
        }

        Payment savedPayment = paymentRepository.save(payment);
        log.info("Payment created with id: {} for orderId: {}", savedPayment.getId(), savedPayment.getYookassaPaymentId());

        PaymentEvent paymentEvent = new PaymentEvent(
                UUID.randomUUID().toString(),
                savedPayment.getOrderId(),
                savedPayment.getStatus(),
                new Timestamp(System.currentTimeMillis())
        );
        kafkaProducer.sendPaymentEvent(paymentEvent);
        log.info("Payment event sent to Kafka for orderId: {}, status: {}", savedPayment.getOrderId(), savedPayment.getStatus());

        return paymentResponseMapper.toResponse(savedPayment);
    }

    @Override
    public PaymentResponse getPaymentById(Long orderId) {
        Payment payment = paymentRepository.findById(orderId)
                .orElseThrow(() -> new PaymentNotFoundException("Payment not found with id: " + orderId));
        return paymentResponseMapper.toResponse(payment);
    }

    @Override
    public Page<PaymentResponse> getPaymentsByOrderId(Long orderId, Pageable pageable) {
        Page<Payment> payments = paymentRepository.findByOrderId(orderId, pageable);
        return payments.map(paymentResponseMapper::toResponse);
    }

    @Override
    public Page<PaymentResponse> getAllPayments(Pageable pageable) {
        return paymentRepository.findAll(pageable)
                .map(paymentResponseMapper::toResponse);
    }

    public void handleYookassaWebhook(String payload, String signature){
        log.info("Handling Yookassa webhook: {}", payload);
        try{
            Map<String, Object> webhookData = objectMapper.readValue(payload, Map.class);
            Map<String, Object> object = (Map<String, Object>) webhookData.get("object");

            String yookassaPaymentId = (String)object.get("id");
            String status = (String) object.get("status");

            log.info("Webhook: yookassaPaymentId={}, status={}", yookassaPaymentId, status);

            Payment payment = paymentRepository.findByYookassaPaymentId(yookassaPaymentId).orElse(null);

            if (payment == null) {
                log.warn("Payment not found in database for yookassaPaymentId: {}. Webhook ignored.", yookassaPaymentId);
                return;
            }

            Status oldStatus = payment.getStatus();
            Status newStatus = Status.valueOf(status.toUpperCase());

            payment.setStatus(newStatus);
            payment.setUpdatedAt(new java.sql.Timestamp(System.currentTimeMillis()));

            kafkaProducer.sendPaymentEvent(new PaymentEvent(null,
                    payment.getOrderId(),
                    newStatus,
                    new Timestamp(System.currentTimeMillis())));

            paymentRepository.save(payment);
            log.info("Payment {} status updated: {} -> {}", payment.getId(), oldStatus, newStatus);


        }catch (Exception e){
            log.error("Failed to process webhook: {}", e.getMessage(), e);
        }
    }

    private Map<String, Object> buildYookassaPaymentRequest(BigDecimal amount, PaymentCreateRequest request){
        Map<String, Object> paymentRequest = new HashMap<>();

        Map<String, String> amountMap = new HashMap<>();
        amountMap.put("value", amount.toString());
        amountMap.put("currency", "RUB");
        paymentRequest.put("amount", amountMap);

        paymentRequest.put("capture", true);

        Map<String, Object> confirmation = new HashMap<>();
        confirmation.put("type", "redirect");
        confirmation.put("return_url", request.returnUrl() != null ? request.returnUrl() : "http://localhost:8080/payments/success");
        paymentRequest.put("confirmation", confirmation);

        paymentRequest.put("description", request.description() != null ? request.description() : "Payment for order #" + request.orderId());

        Map<String, Object> metadata = new HashMap<>();
        metadata.put("order_id", request.orderId().toString());
        paymentRequest.put("metadata", metadata);

        return paymentRequest;
    }

    @Override
    public PaymentResponse refundPayment(Long paymentId, RefundRequest request) {
        Payment payment = paymentRepository.findById(paymentId)
                .orElseThrow(() -> new PaymentNotFoundException("Payment not found with id: " + paymentId));

        if (!payment.getStatus().equals(Status.SUCCEEDED)) {
            throw new RefundFailedException("Can only refund succeeded payments");
        }

        BigDecimal amount = request.amount() != null ? request.amount() : payment.getAmount();

        try{
            Map<String, Object> refundRequest = new HashMap<>();
            refundRequest.put("payment_id", payment.getYookassaPaymentId());

            Map<String, Object> amountMap = new HashMap<>();
            amountMap.put("value", amount.toString());
            amountMap.put("currency", "RUB");
            refundRequest.put("amount", amountMap);

            if(request.reason() != null){
                refundRequest.put("description", request.reason());
            }

            log.info("Sending refund request for payment: {}", paymentId);

            yookassaFeignClient.createRefund(UUID.randomUUID().toString(), refundRequest);

            payment.setStatus(Status.REFUNDED);
            payment.setMetadata("Refund: " + amount + " RUB");

            Payment savedPayment = paymentRepository.save(payment);

            log.info("Payment {} refunded: {}", paymentId, amount);

            PaymentEvent refundEvent = new PaymentEvent(
                    UUID.randomUUID().toString(),
                    savedPayment.getOrderId(),
                    Status.REFUNDED,
                    new Timestamp(System.currentTimeMillis())
            );
            kafkaProducer.sendPaymentEvent(refundEvent);

            log.info("Refund event sent to Kafka for orderId: {}", savedPayment.getOrderId());

            return paymentResponseMapper.toResponse(savedPayment);

        }catch (Exception e){
            log.error("Failed to refund payment: {}", e.getMessage(), e);
            payment.setMetadata("Refund failed: " + e.getMessage());
            paymentRepository.save(payment);
            throw new RefundFailedException("Failed to refund payment: " + e.getMessage());
        }
    }
}
