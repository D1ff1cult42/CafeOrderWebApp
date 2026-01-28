package org.example.apigateway.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import org.example.common.kafka.Status;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.List;

@Schema(
        name = "AggregatedOrderResponse",
        description = "Агрегированный ответ с полной информацией о заказе и пиццах"
)
public record AggregatedOrderResponse(
        @Schema(description = "Уникальный идентификатор заказа", example = "1", required = true)
        Long orderId,

        @Schema(description = "Имя заказчика", example = "Иван Иванов", required = true)
        String customerName,

        @Schema(description = "Адрес заказчика", example = "г. Москва, ул. Пушкина, д. 10", required = true)
        String address,

        @Schema(description = "Телефон заказчика", example = "+7 (912) 345-67-89", required = true)
        String phone,

        @Schema(description = "Статус заказа", example = "CREATED", required = true)
        Status status,

        @Schema(description = "Общая стоимость заказа", example = "1500.75", required = true)
        BigDecimal totalPrice,

        @Schema(description = "Список пицц в заказе с полной информацией", required = true)
        List<PizzaInfo> pizzas,

        @Schema(description = "Временная метка создания заказа", example = "2024-06-01T12:00:00Z", required = true)
        Timestamp createdAt,

        @Schema(description = "Временная метка последнего обновления заказа", example = "2024-06-01T12:30:00Z", required = true)
        Timestamp updatedAt
) {
    @Schema(name = "PizzaInfo", description = "Информация о пицце в заказе")
    public record PizzaInfo(
            @Schema(description = "Уникальный идентификатор пиццы", example = "14")
            Long id,

            @Schema(description = "Название пиццы", example = "Маргарита")
            String name,

            @Schema(description = "Цена пиццы", example = "499.99")
            BigDecimal price,

            @Schema(description = "Флаг, указывающий на существование пиццы", example = "true")
            Boolean isExists
    ) {}
}
