package org.example.apigateway.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import org.example.common.kafka.Status;

import java.math.BigDecimal;
import java.sql.Timestamp;

@Schema(
        name = "AggregatedPaymentResponse",
        description = "Агрегированный ответ с полной информацией о платеже и заказе"
)
public record AggregatedPaymentResponse(
        @Schema(description = "Уникальный идентификатор платежа", example = "1")
        Long paymentId,

        @Schema(description = "Сумма платежа", example = "1500.75")
        BigDecimal amount,

        @Schema(description = "Статус платежа", example = "PENDING")
        Status paymentStatus,

        @Schema(description = "ID платежа в ЮKassa", example = "2c5d8dca-000f-5000-9000-145f6df2e1b2")
        String yookassaPaymentId,

        @Schema(description = "URL для подтверждения платежа", example = "https://yookassa.ru/checkout/payments/2c5d8dca-000f-5000-9000-145f6df2e1b2")
        String confirmationUrl,

        @Schema(description = "Временная метка создания платежа", example = "2024-06-01T12:00:00Z")
        Timestamp paymentCreatedAt,

        @Schema(description = "Временная метка последнего обновления платежа", example = "2024-06-01T12:30:00Z")
        Timestamp paymentUpdatedAt,

        @Schema(description = "Информация о заказе")
        OrderInfo order
) {
    @Schema(name = "OrderInfo", description = "Информация о заказе в платеже")
    public record OrderInfo(
            @Schema(description = "Уникальный идентификатор заказа", example = "1")
            Long id,

            @Schema(description = "Имя заказчика", example = "Иван Иванов")
            String name,

            @Schema(description = "Адрес заказчика", example = "г. Москва, ул. Пушкина, д. 10")
            String address,

            @Schema(description = "Телефон заказчика", example = "+7 (912) 345-67-89")
            String phone,

            @Schema(description = "Статус заказа", example = "CREATED")
            Status status,

            @Schema(description = "Общая стоимость заказа", example = "1500.75")
            BigDecimal totalPrice,

            @Schema(description = "Временная метка создания заказа", example = "2024-06-01T12:00:00Z")
            Timestamp createdAt,

            @Schema(description = "Временная метка последнего обновления заказа", example = "2024-06-01T12:30:00Z")
            Timestamp updatedAt
    ) {}
}

