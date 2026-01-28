package org.example.apigateway.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.apigateway.dto.AggregatedOrderResponse;
import org.example.apigateway.dto.AggregatedPaymentResponse;
import org.example.apigateway.service.AggregationService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@Tag(name = "Aggregated Rest Controller", description = "Агрегированные операции с данными из нескольких сервисов")
@RequiredArgsConstructor
@RequestMapping("/aggregated")
public class AggregatedRestController {

    private final AggregationService aggregationService;

    @Operation(
            summary = "Получение заказа с полной информацией о пиццах",
            description = "Позволяет получить информацию о заказе со всеми деталями пицц из меню",
            responses = {
                    @io.swagger.v3.oas.annotations.responses.ApiResponse(
                            responseCode = "200",
                            description = "Заказ успешно получен",
                            content = @io.swagger.v3.oas.annotations.media.Content(
                                    mediaType = "application/json",
                                    schema = @io.swagger.v3.oas.annotations.media.Schema(implementation = AggregatedOrderResponse.class)
                            )
                    ),
                    @io.swagger.v3.oas.annotations.responses.ApiResponse(
                            responseCode = "404",
                            description = "Заказ с указанным ID не найден"
                    )
            }
    )
    @Parameter(name = "orderId", description = "Уникальный идентификатор заказа", required = true, example = "1")
    @GetMapping("/orders/{orderId}")
    public ResponseEntity<AggregatedOrderResponse> getOrderWithPizzas(@PathVariable Long orderId) {
        log.info("GET /aggregated/orders/{} - Retrieving order with pizza details", orderId);
        return ResponseEntity.ok(aggregationService.getOrderWithPizzas(orderId));
    }

    @Operation(
            summary = "Получение платежа с полной информацией о заказе",
            description = "Позволяет получить информацию о платеже со всеми деталями связанного заказа",
            responses = {
                    @io.swagger.v3.oas.annotations.responses.ApiResponse(
                            responseCode = "200",
                            description = "Платеж успешно получен",
                            content = @io.swagger.v3.oas.annotations.media.Content(
                                    mediaType = "application/json",
                                    schema = @io.swagger.v3.oas.annotations.media.Schema(implementation = AggregatedPaymentResponse.class)
                            )
                    ),
                    @io.swagger.v3.oas.annotations.responses.ApiResponse(
                            responseCode = "404",
                            description = "Платеж с указанным ID не найден"
                    )
            }
    )
    @Parameter(name = "paymentId", description = "Уникальный идентификатор платежа", required = true, example = "1")
    @GetMapping("/payments/{paymentId}")
    public ResponseEntity<AggregatedPaymentResponse> getPaymentWithOrder(@PathVariable Long paymentId) {
        log.info("GET /aggregated/payments/{} - Retrieving payment with order details", paymentId);
        return ResponseEntity.ok(aggregationService.getPaymentWithOrder(paymentId));
    }
}

