package org.example.paymentservice.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import org.example.common.kafka.Status;

import java.math.BigDecimal;
import java.sql.Timestamp;

@Schema(
        name = "PaymentResponse",
        description = "Ответ с информацией о платеже"
)
public record PaymentResponse(
        @Schema(
                description = "Уникальный идентификатор платежа",
                example = "1",
                required = true
        )
        Long id,
        @Schema(
                description = "Уникальный идентификатор заказа",
                example = "1001",
                required = true
        )
        Long orderId,
        @Schema(
                description = "Сумма платежа",
                example = "1500.75",
                required = true
        )
        BigDecimal amount,
        @Schema(
                description = "Статус платежа",
                example = "PENDING",
                required = true
        )
        Status status,
        @Schema(
                description = "Идентификатор платежа в системе ЮKassa",
                example = "2c5d8dca-000f-5000-9000-145f6df2e1b2"
        )
        String yookassaPaymentId,
        @Schema(
                description = "URL для подтверждения платежа",
                example = "https://yookassa.ru/checkout/payments/2c5d8dca-000f-5000-9000-145f6df2e1b2"
        )
        String confirmationUrl,
        @Schema(
                description = "Временная метка создания платежа",
                example = "2024-06-01T12:00:00Z",
                required = true
        )
        Timestamp createdAt,
        @Schema(
                description = "Временная метка последнего обновления платежа",
                example = "2024-06-01T12:30:00Z",
                required = true
        )
        Timestamp updatedAt) {}
