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
import org.example.paymentservice.dto.response.PaymentResponse;
import org.example.paymentservice.service.interfaces.PaymentService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@Slf4j
@RestController
@RequestMapping("/payments")
@RequiredArgsConstructor
@Tag(name = "Payment Rest Controller", description = "Операции с платежами")
public class PaymentRestController {
    private final PaymentService paymentService;

    @Operation(
            summary = "Получение платежа по ID",
            description = "Позволяет получить информацию о платеже по его уникальному идентификатору",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Платеж успешно получен",
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
                    )
            }
    )
    @Parameter(
            name = "id",
            description = "Уникальный идентификатор платежа",
            required = true,
            example = "1"
    )
    @GetMapping("/{id}")
    public ResponseEntity<PaymentResponse> getPaymentById(@PathVariable Long id) {
        log.info("GET /payments/{id} - Retrieving payment with id: {}", id);
        return ResponseEntity.ok(paymentService.getPaymentById(id));
    }


    @Operation(
            summary = "Получение платежей по ID заказа",
            description = "Позволяет получить список платежей для конкретного заказа с пагинацией",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Платежи успешно получены",
                            content = @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = Page.class)
                            )
                    ),
                    @ApiResponse(
                            responseCode = "404",
                            description = "Заказ с указанным ID не найден",
                            content = @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = ErrorResponse.class)
                            )
                    )
            }
    )
    @Parameter(
            name = "orderId",
            description = "Уникальный идентификатор заказа",
            required = true,
            example = "1001"
    )
    @GetMapping("/order/{orderId}")
    public ResponseEntity<Page<PaymentResponse>> getPaymentByOrderId(@PathVariable Long orderId,
                                                                     @RequestParam(defaultValue = "0")
                                                                     @Parameter(description = "Номер страницы", example = "0") int page,
                                                                     @RequestParam(defaultValue = "10")
                                                                     @Parameter(description = "Размер страницы", example = "10") int size) {
        log.info("GET /payments/order/{orderId} - Retrieving payments for orderId: {}, page: {}, size: {}", orderId, page, size);
        Pageable pageable = PageRequest.of(page, size);
        Page<PaymentResponse> paymentResponsePage = paymentService.getPaymentsByOrderId(orderId, pageable);
        return ResponseEntity.ok(paymentResponsePage);
    }
}
