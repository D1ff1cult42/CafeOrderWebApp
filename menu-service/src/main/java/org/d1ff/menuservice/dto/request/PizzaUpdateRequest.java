package org.d1ff.menuservice.dto.request;


import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Positive;

import java.math.BigDecimal;


@Schema(name = "PizzaUpdateRequest",
        description = "Запрос для обновления информации о пицце")
public record PizzaUpdateRequest(
        @Schema(
                description = "Название пиццы",
                example = "Пепперони",
                required = false
        )
        String name,
        @Positive
        @Schema(
                description = "Цена пиццы",
                example = "599.99",
                required = false
        )
        BigDecimal price,
        @Schema(
                description = "Флаг, указывающий на существование пиццы",
                example = "true",
                required = false
        )
        Boolean isExists
        ) {}

