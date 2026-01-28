package org.d1ff.menuservice.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.tags.Tag;

import java.math.BigDecimal;

@Schema(
        name  = "PizzaResponse",
        description = "Ответ с информацией о пицце"
)
public record PizzaResponse(
        @Schema(
                description = "Уникальный идентификатор пиццы",
                example = "14",
                required = true
        )
        Long id,
        @Schema(
                description = "Название пиццы",
                example = "Маргарита",
                required = true
        )
        String name,
        @Schema(
                description = "Цена пиццы",
                example = "499.999",
                required = true
        )
        BigDecimal price,
        @Schema(
                description = "Флаг, указывающий на существование пиццы",
                example = "true",
                required = true
        )
        Boolean isExists) {}
