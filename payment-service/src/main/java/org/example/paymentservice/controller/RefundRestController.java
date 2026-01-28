package org.example.paymentservice.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.paymentservice.dto.request.RefundRequest;
import org.example.paymentservice.dto.response.ErrorResponse;
import org.example.paymentservice.dto.response.PaymentResponse;
import org.example.paymentservice.service.interfaces.PaymentService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@RequestMapping("/payments/{id}/refund")
@RestController
@RequiredArgsConstructor
@Slf4j
@Tag(name = "Refund Rest Controller", description = "Операции возврата платежей")
public class RefundRestController {
    private final PaymentService paymentService;

    @Operation(
            summary = "Возврат платежа",
            description = "Позволяет выполнить частичный или полный возврат средств по платежу",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Возврат успешно выполнен",
                            content = @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = PaymentResponse.class)
                            )
                    ),
                    @ApiResponse(
                            responseCode = "404",
                            description = "Платеж с указанным ID не найден",
                            content = @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = ErrorResponse.class)
                            )
                    ),
                    @ApiResponse(
                            responseCode = "400",
                            description = "Некорректная сумма возврата или платеж не может быть возвращен",
                            content = @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = ErrorResponse.class)
                            )
                    )
            }
    )
    @Parameter(
            name = "id",
            description = "Уникальный идентификатор платежа",
            required = true,
            example = "1"
    )
    @PostMapping
    public ResponseEntity<PaymentResponse> refund(@PathVariable Long id, @RequestBody @Valid RefundRequest refundRequest) {
        log.info("POST /payments/{}/refund - Refunding payment with id: {} for amount: {}", id, id, refundRequest.amount());
        PaymentResponse response = paymentService.refundPayment(id, refundRequest);
        return ResponseEntity.ok(response);
    }
}
