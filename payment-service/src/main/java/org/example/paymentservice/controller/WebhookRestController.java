package org.example.paymentservice.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.paymentservice.dto.response.ErrorResponse;
import org.example.paymentservice.service.interfaces.PaymentService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/payments/webhook")
@RequiredArgsConstructor
@Slf4j
@Tag(name = "Webhook Rest Controller", description = "Обработка webhook-уведомлений от ЮKassa")
public class WebhookRestController {
    private final PaymentService paymentService;

    @Operation(
            summary = "Обработка webhook от ЮKassa",
            description = "Принимает уведомления от платежной системы ЮKassa о изменении статуса платежа",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Webhook успешно обработан"
                    ),
                    @ApiResponse(
                            responseCode = "400",
                            description = "Некорректная подпись или формат данных",
                            content = @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = ErrorResponse.class)
                            )
                    )
            }
    )
    @PostMapping
    public ResponseEntity<Void> handleWebhook(@RequestBody
                                              @Parameter(description = "JSON payload от ЮKassa") String payload,
                                              @RequestHeader(value = "X-Signature", required = false)
                                              @Parameter(description = "Подпись запроса для проверки подлинности") String signature) {
        log.info("POST /payments/webhook - Received webhook with payload: {} and signature: {}", payload, signature);
        paymentService.handleYookassaWebhook(payload, signature);
        return ResponseEntity.ok().build();
    }
}
