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
import org.example.paymentservice.dto.request.PaymentCreateRequest;
import org.example.paymentservice.dto.response.ErrorResponse;
import org.example.paymentservice.dto.response.PaymentResponse;
import org.example.paymentservice.service.interfaces.PaymentService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;

@RestController
@RequestMapping("/payments")
@RequiredArgsConstructor
@Slf4j
@Tag(name = "Payments Rest Controller", description = "Создание платежей и получение списка")
public class PaymentsRestController {
    private final PaymentService paymentService;

    @Operation(
            summary = "Создание нового платежа",
            description = "Позволяет создать новый платеж для заказа и получить ссылку на оплату",
            responses = {
                    @ApiResponse(
                            responseCode = "201",
                            description = "Платеж успешно создан",
                            content = @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = PaymentResponse.class)
                            )
                    ),
                    @ApiResponse(
                            responseCode = "400",
                            description = "Некорректные данные запроса",
                            content = @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = ErrorResponse.class)
                            )
                    ),
                    @ApiResponse(
                            responseCode = "404",
                            description = "Заказ не найден",
                            content = @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = ErrorResponse.class)
                            )
                    )
            }
    )
    @PostMapping
    public ResponseEntity<PaymentResponse> createPayment(@RequestBody @Valid PaymentCreateRequest paymentRequest) {
        log.info("POST /payments - Creating payment for request: {}", paymentRequest.orderId());
        PaymentResponse paymentResponse = paymentService.createPayment(paymentRequest);
        return ResponseEntity.created(URI.create("payments/")).body(paymentResponse);
    }

    @Operation(
            summary = "Получение списка всех платежей",
            description = "Позволяет получить список всех платежей с пагинацией",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Список платежей успешно получен",
                            content = @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = Page.class)
                            )
                    )
            }
    )
    @GetMapping
    public ResponseEntity<Page<PaymentResponse>> getAllPayments(@RequestParam(defaultValue = "0")
                                                                @Parameter(description = "Номер страницы", example = "0") int page,
                                                                @RequestParam(defaultValue = "10")
                                                                @Parameter(description = "Размер страницы", example = "10") int size) {
        log.info("GET /payments - Retrieving all payments, page: {}, size: {}", page, size);
        Pageable pageable = PageRequest.of(page, size);
        Page<PaymentResponse> paymentResponsePage = paymentService.getAllPayments(pageable);
        return ResponseEntity.ok(paymentResponsePage);
    }
}
