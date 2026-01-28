package org.d1ff.menuservice.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

import java.math.BigDecimal;


@Schema(name = "PizzaCreateRequest",
        description = "Запрос для создания новой пиццы")

public record PizzaCreateRequest(
        @Schema(
                description = "Название пиццы",
                example = "Маргарита",
                required = true
        )
        @NotNull(message = "Name cannot be null")
        @NotBlank
        String name,
        @Schema(
                description = "Цена пиццы",
                example = "499.99",
                required = true
        )
        @NotNull(message = "Price cannot be null")
        @Positive(message = "Price must be positive")
        BigDecimal price
) {}
