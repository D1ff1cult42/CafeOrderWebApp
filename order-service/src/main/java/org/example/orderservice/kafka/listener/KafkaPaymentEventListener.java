package org.example.orderservice.kafka.listener;


import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.common.kafka.model.PaymentEvent;
import org.example.orderservice.exception.DatabaseException;
import org.example.orderservice.exception.NotFoundException;
import org.example.orderservice.exception.ValidationException;
import org.example.orderservice.service.interfaces.OrderService;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.support.Acknowledgment;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Slf4j
@RequiredArgsConstructor
public class KafkaPaymentEventListener {

    private final OrderService orderService;

    @KafkaListener(topics = "payment-events")
    @Transactional
    public void handlePaymentEvent(
            @Payload PaymentEvent event,
            @Header(KafkaHeaders.RECEIVED_KEY) String orderId,
            Acknowledgment acknowledgment) {

        try {
            log.info("Processing payment event for order: {}, status: {}", orderId, event.getPaymentStatus());

            processPaymentEvent(event);

            acknowledgment.acknowledge();

            log.info("Payment event processed and acknowledged for order: {}", orderId);

        } catch (ValidationException e) {
            log.error("Validation error, skipping message: {}", e.getMessage());
            acknowledgment.acknowledge();

        } catch (NotFoundException e) {
            log.error("Order not found, skipping message: {}", e.getMessage());
            acknowledgment.acknowledge();

        } catch (DatabaseException e) {
            log.error("Database error, will retry: {}", e.getMessage());
            throw e;

        } catch (Exception e) {
            log.error("Unexpected error processing payment event: {}", e.getMessage(), e);
            throw new DatabaseException("Failed to process payment event", e);
        }
    }

    private void processPaymentEvent(PaymentEvent event) {
        if (event.getOrderId() == null) {
            throw new ValidationException("Order ID is null");
        }

        log.info("Updating order {} status to {}", event.getOrderId(), event.getPaymentStatus());

        orderService.updateOrderStatus(event.getOrderId(), event.getPaymentStatus());

        log.info("Order {} status successfully updated to {}", event.getOrderId(), event.getPaymentStatus());
    }
}
