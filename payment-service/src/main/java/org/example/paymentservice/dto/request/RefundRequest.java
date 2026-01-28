package org.example.paymentservice.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Positive;

import java.math.BigDecimal;

@Schema(
        name = "RefundRequest",
        description = "Запрос для возврата платежа"
)
public record RefundRequest(
        @Schema(
                description = "Сумма возврата",
                example = "500.00",
                required = true
        )
        @Positive(message = "Refund amount must be positive")
        BigDecimal amount,

        @Schema(
                description = "Причина возврата",
                example = "Отмена заказа по запросу клиента"
        )
        String reason){}
