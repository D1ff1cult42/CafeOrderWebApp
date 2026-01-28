package org.example.paymentservice.service.interfaces;

import org.example.paymentservice.dto.request.PaymentCreateRequest;
import org.example.paymentservice.dto.request.RefundRequest;
import org.example.paymentservice.dto.response.PaymentResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface PaymentService {
    PaymentResponse createPayment(PaymentCreateRequest request);

    PaymentResponse getPaymentById(Long orderId);

    Page<PaymentResponse> getPaymentsByOrderId(Long orderId, Pageable pageable);

    void handleYookassaWebhook(String payload, String signature);

    PaymentResponse refundPayment(Long paymentId, RefundRequest refundRequest);

    Page<PaymentResponse> getAllPayments(Pageable pageable);
}
