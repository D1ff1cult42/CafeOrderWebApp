package org.example.apigateway.client;

import org.example.common.kafka.Status;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.List;

@FeignClient(name = "order-service", url = "${order.service.url}")
public interface OrderServiceFeignClient {

    @GetMapping("/orders/{id}")
    OrderResponse getOrder(@PathVariable("id") Long orderId);

    record OrderResponse(Long id,
                         String name,
                         String address,
                         String phone,
                         Status status,
                         List<Long> pizzaIds,
                         BigDecimal totalPrice,
                         Timestamp createdAt,
                         Timestamp updatedAt) {
    }
}
