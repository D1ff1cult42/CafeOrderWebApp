package org.example.paymentservice.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;

import java.util.Date;

@Schema(
        name = "ErrorResponse",
        description = "Ответ с информацией об ошибке"
)
public record ErrorResponse(
    @Schema(
            description = "Временная метка ошибки",
            example = "2024-06-01T12:00:00Z",
            required = true
    )
    Date timestamp,
    @Schema(
            description = "HTTP статус ошибки",
            example = "404",
            required = true
    )
    int status,
    @Schema(
            description = "Сообщение об ошибке",
            example = "Order not found",
            required = true
    )
    String message
){}
