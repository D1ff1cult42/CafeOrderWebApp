package org.example.paymentservice.kafka.producer;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.common.kafka.model.PaymentEvent;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;
import org.springframework.stereotype.Service;

import java.util.UUID;
import java.util.concurrent.CompletableFuture;

@Slf4j
@Service
@RequiredArgsConstructor
public class KafkaPaymentEventProducer {
    private final KafkaTemplate<String, Object> paymentKafkaTemplate;

    @Value("${payment.kafka.topic-payment-events}")
    private String paymentEventsTopic;

    public void sendPaymentEvent(PaymentEvent event) {
        Long key = event.getOrderId();
        if (event.getEventId() == null) {
            event.setEventId(UUID.randomUUID().toString());
        }
        log.info("Sending Payment Event to Kafka topic: {}, status: {}", paymentEventsTopic, event.getPaymentStatus());
        CompletableFuture<SendResult<String, Object>> future =
                paymentKafkaTemplate.send(paymentEventsTopic, key.toString(), event);

        future.whenComplete((result, ex) -> {
            if (ex != null) {
                log.error("Failed to send Payment Event with key: {} to topic: {}. Error: {}", key, paymentEventsTopic, ex.getMessage());
            } else {
                log.info("Successfully sent Payment Event with key: {} to topic: {}, partition: {}, offset: {}",
                        key, paymentEventsTopic, result.getRecordMetadata().partition(), result.getRecordMetadata().offset());
            }
        });
    }
}
