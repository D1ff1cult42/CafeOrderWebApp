package org.example.orderservice.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.Parameters;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.example.orderservice.dto.request.OrderCreateRequest;
import org.example.orderservice.dto.response.OrderResponse;
import org.example.orderservice.service.interfaces.OrderService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;

@RestController
@Tag(
        name = "Orders Rest Controller",
        description = "Операции с заказами"
)
@RequiredArgsConstructor
@RequestMapping("/orders")
public class OrdersRestController {
    private final OrderService orderService;

    @Operation(
            summary = "Создание нового заказа",
            description = "Позволяет создать новый заказ",
            responses = {
                    @io.swagger.v3.oas.annotations.responses.ApiResponse(
                            responseCode = "201",
                            description = "Заказ успешно создан",
                            content = @io.swagger.v3.oas.annotations.media.Content(
                                    mediaType = "application/json",
                                    schema = @io.swagger.v3.oas.annotations.media.Schema(implementation = OrderResponse.class)
                            )
                    )
            }
    )
    @Parameter(name = "orderCreateRequest",
            description = "Данные для создания нового заказа",
            required = true,
            schema = @io.swagger.v3.oas.annotations.media.Schema(
                    implementation = OrderCreateRequest.class
            )

    )
    @PostMapping
    public ResponseEntity<OrderResponse> createOrder(@RequestBody @Valid OrderCreateRequest orderCreateRequest){
        OrderResponse orderResponse = orderService.createOrder(orderCreateRequest);
        return ResponseEntity.created(URI.create("orders/" + orderResponse.id()))
                .body(orderResponse);
    }

    @Operation(
            summary = "Получение всех заказов с пагинацией",
            description = "Позволяет получить список всех заказов с возможностью пагинации",
            responses = {
                    @io.swagger.v3.oas.annotations.responses.ApiResponse(
                            responseCode = "200",
                            description = "Список заказов успешно получен",
                            content = @io.swagger.v3.oas.annotations.media.Content(
                                    mediaType = "application/json",
                                    schema = @io.swagger.v3.oas.annotations.media.Schema(implementation = Page.class)
                            )
                    )
            }
    )
    @Parameters({
            @Parameter(name = "page", description = "Номер страницы для пагинации", example = "0"),
            @Parameter(name = "size", description = "Количество элементов на странице", example = "10")
    })
    @GetMapping
    public ResponseEntity<Page<OrderResponse>> getAllOrders(@RequestParam(defaultValue = "0") int page,
                                                            @RequestParam(defaultValue = "10") int size) {
        Pageable pageable = PageRequest.of(page, size);
        Page<OrderResponse> orders = orderService.getAllOrders(pageable);
        return ResponseEntity.ok(orders);
    }

    @Operation(
            summary = "Получение всех заказов по идентификаторам пицц с пагинацией",
            description = "Позволяет получить список всех заказов, содержащих указанные пиццы, с возможностью пагинации",
            responses = {
                    @io.swagger.v3.oas.annotations.responses.ApiResponse(
                            responseCode = "200",
                            description = "Список заказов успешно получен",
                            content = @io.swagger.v3.oas.annotations.media.Content(
                                    mediaType = "application/json",
                                    schema = @io.swagger.v3.oas.annotations.media.Schema(implementation = Page.class)
                            )
                    )
            }
    )
    @Parameters({
            @Parameter(name = "pizzaIds", description = "Список уникальных идентификаторов", example = "[1, 2, 3]", required = true),
            @Parameter(name = "page", description = "Номер страницы для пагинации", example = "0"),
            @Parameter(name = "size", description = "Количество элементов на странице", example = "10")
    })
    @GetMapping("/by-pizza")
    public ResponseEntity<Page<OrderResponse>> getAllOrdersByPizzaId(@RequestParam List<Long> pizzaIds,
                                                                     @RequestParam(defaultValue = "0") int page,
                                                                     @RequestParam(defaultValue = "10") int size) {
        Pageable pageable = PageRequest.of(page, size);
        Page<OrderResponse> orders = orderService.getAllOrdersByPizzaId(pizzaIds, pageable);
        return ResponseEntity.ok(orders);
    }
}
