package org.example.paymentservice.config;


import jakarta.validation.ValidationException;
import org.apache.kafka.common.TopicPartition;
import org.example.paymentservice.exceptions.PaymentNotFoundException;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.KafkaException;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.listener.DeadLetterPublishingRecoverer;
import org.springframework.kafka.listener.DefaultErrorHandler;
import org.springframework.kafka.support.ExponentialBackOffWithMaxRetries;

@Configuration
public class KafkaErrorConfig {

    @Bean
    public DefaultErrorHandler errorHandler(DeadLetterPublishingRecoverer deadLetterPublishingRecoverer) {
            ExponentialBackOffWithMaxRetries backOff = new ExponentialBackOffWithMaxRetries(3);
            backOff.setInitialInterval(1000L);
            backOff.setMultiplier(2.0);
            backOff.setMaxInterval(10000L);

            DefaultErrorHandler errorHandler = new DefaultErrorHandler(
                    deadLetterPublishingRecoverer,
                    backOff
            );

            errorHandler.addNotRetryableExceptions(
                    IllegalArgumentException.class,
                    ValidationException.class,
                    PaymentNotFoundException.class
            );

            errorHandler.setLogLevel(KafkaException.Level.WARN);

            return errorHandler;
    }

    @Bean
    public DeadLetterPublishingRecoverer deadLetterPublishingRecoverer(
            KafkaTemplate<String, Object> kafkaTemplate) {

        return new DeadLetterPublishingRecoverer(kafkaTemplate,
                (consumerRecord, exception) -> {
            String topic = consumerRecord.topic();

            if (exception.getCause() instanceof ValidationException) {
                return new TopicPartition(topic + ".validation.dlt", consumerRecord.partition());
            } else {
                return new TopicPartition(topic + ".dlt", consumerRecord.partition());
            }
        });
    }
}
