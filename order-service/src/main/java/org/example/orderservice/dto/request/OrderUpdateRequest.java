package org.example.orderservice.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(name = "OrderUpdateRequest",
        description = "Запрос для обновления информации о заказе")
public record OrderUpdateRequest(
        @Schema(
                description = "Имя заказчика",
                example = "Иван Иванов",
                required = false
        )
        String name,
        @Schema(
                description = "Адрес заказчика",
                example = "г. Москва , ул. Пушкина, д. 10",
                required = false
        )
        String address,
        @Schema(
                 description = "Телефон заказчика",
                 example = "+7 (912) 345-67-89",
                 required = false
         )
        String phone
                                 ) {
}
