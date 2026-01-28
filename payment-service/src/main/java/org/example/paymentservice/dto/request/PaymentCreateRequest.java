package org.example.paymentservice.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;

@Schema(
        name = "PaymentCreateRequest",
        description = "Запрос для создания нового платежа"
)
public record PaymentCreateRequest(
        @Schema(
                description = "Уникальный идентификатор заказа",
                example = "1001",
                required = true
        )
        @NotNull(message = "Order ID is required")
        Long orderId,

        @Schema(
                description = "Описание платежа",
                example = "Оплата заказа #1001"
        )
        String description,

        @Schema(
                description = "URL для возврата после оплаты",
                example = "https://example.com/payment/success"
        )
        String returnUrl
){}
