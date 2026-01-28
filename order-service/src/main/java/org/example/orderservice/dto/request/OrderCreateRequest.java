package org.example.orderservice.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;

import java.util.List;

@Schema(
        description = "Запрос для создания нового заказа",
        name = "OrderCreateRequest"
)
public record OrderCreateRequest(
        @Schema(
                description = "Имя заказчика",
                example = "Иван Иванов",
                required = true
        )
        @NotBlank(message = "Name cannot be blank")
        @NotNull(message = "Name cannot be null")
        String name,
        @NotNull(message = "Address cannot be null")
        @NotBlank(message = "Address cannot be blank")
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
        @NotNull(message = "Phone cannot be null")
        @NotBlank(message = "Phone cannot be blank")
        @Pattern(
                regexp = "^(\\+7|8)[\\s\\-]?\\(?(9\\d{2})\\)?[\\s\\-]?\\d{3}[\\s\\-]?\\d{2}[\\s\\-]?\\d{2}$",
                message = "Incorrect phone number format")
        String phone,
        @Schema(
                description = "Список идентификаторов пицц в заказе",
                example = "[1, 2, 3]",
                required = true
        )
        @NotNull(message = "Order is blank!")
        List<Long> pizzaIds
        ) {}
