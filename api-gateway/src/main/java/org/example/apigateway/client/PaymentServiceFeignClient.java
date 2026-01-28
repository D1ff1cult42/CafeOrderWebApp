package org.example.apigateway.client;

import org.example.common.kafka.Status;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.math.BigDecimal;
import java.sql.Timestamp;

@FeignClient(name = "payment-service", url = "${payment.service.url}")
public interface PaymentServiceFeignClient {

    @GetMapping("/payments/{id}")
    PaymentResponse getPayment(@PathVariable("id") Long paymentId);

    record PaymentResponse(Long id,
                           Long orderId,
                           BigDecimal amount,
                           Status status,
                           String yookassaPaymentId,
                           String confirmationUrl,
                           Timestamp createdAt,
                           Timestamp updatedAt) {
    }
}
