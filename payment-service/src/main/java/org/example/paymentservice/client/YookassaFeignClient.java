package org.example.paymentservice.client;

import org.example.paymentservice.config.YookassaFeignConfig;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@FeignClient(
        name = "yookassa-client",
        url = "${payment.yookassa.api-url:https://api.yookassa.ru/v3}",
        configuration = YookassaFeignConfig.class
)
public interface YookassaFeignClient {
    @PostMapping("/payments")
    Map<String, Object> createPayment(
            @RequestHeader("Idempotence-Key") String idempotenceKey,
            @RequestBody Map<String, Object> paymentRequest
    );

    @GetMapping("/payments/{paymentId}")
    Map<String, Object> getPayment(@PathVariable("paymentId") String paymentId);

    @PostMapping("/refunds")
    Map<String, Object> createRefund(
            @RequestHeader("Idempotence-Key") String idempotenceKey,
            @RequestBody Map<String, Object> refundRequest
    );
}
