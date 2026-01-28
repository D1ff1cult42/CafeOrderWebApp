package org.example.orderservice.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import org.example.common.kafka.Status;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.List;

@Schema(
        name = "OrderResponse",
        description = "Ответ с информацией о заказе"
)
public record OrderResponse(
        @Schema(
                description = "Уникальный идентификатор заказа",
                example = "1",
                required = true
        )
        Long id,
        @Schema(
                description = "Имя заказчика",
                example = "Иван Иванов",
                required = true
        )
        String name,
        @Schema(
                description = "Адрес заказчика",
                example = "г. Москва , ул. Пушкина, д. 10",
                required = true
        )
        String address,
        @Schema(
                description = "Телефон заказчика",
                example = "+7 (912) 345-67-89",
                required = true
        )
        String phone,
        @Schema(
                description = "Статус заказа",
                example = "CREATED",
                required = true
        )
        Status status,
        @Schema(
                description = "Список идентификаторов пицц в заказе",
                example = "[1, 2, 3]",
                required = true
        )
        List<Long> pizzaIds,
        @Schema(
                description = "Общая стоимость заказа",
                example = "1500.75",
                required = true
        )
        BigDecimal totalPrice,
        @Schema(
                description = "Временная метка создания заказа",
                example = "2024-06-01T12:00:00Z",
                required = true
        )
        Timestamp createdAt,
        @Schema(
                description = "Временная метка последнего обновления заказа",
                example = "2024-06-01T12:30:00Z",
                required = true
        )
        Timestamp updatedAt) {}


