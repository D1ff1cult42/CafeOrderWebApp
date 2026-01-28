package org.example.orderservice.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.Parameters;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.example.orderservice.dto.request.OrderUpdateRequest;
import org.example.orderservice.dto.response.OrderResponse;
import org.example.orderservice.service.interfaces.OrderService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@Tag(name = "Order Rest Controller", description = "Операции с заказами")
@RequiredArgsConstructor
@RequestMapping("/orders/{id}")
public class OrderRestController {
    private final OrderService orderService;

    @Operation(
            summary = "Получение заказа по ID",
            description = "Позволяет получить информацию о заказе по его уникальному идентификатору",
            responses = {
                    @io.swagger.v3.oas.annotations.responses.ApiResponse(
                            responseCode = "200",
                            description = "Заказ успешно получен",
                            content = @io.swagger.v3.oas.annotations.media.Content(
                                    mediaType = "application/json",
                                    schema = @io.swagger.v3.oas.annotations.media.Schema(implementation = OrderResponse.class)
                            )
                    ),
                    @io.swagger.v3.oas.annotations.responses.ApiResponse(
                            responseCode = "404",
                            description = "Заказ с указанным ID не найден",
                            content = @io.swagger.v3.oas.annotations.media.Content(
                                    mediaType = "application/json",
                                    schema = @io.swagger.v3.oas.annotations.media.Schema(implementation = org.example.orderservice.dto.response.ErrorResponse.class)
                            )
                    )
            }
    )
    @Parameter(name = "id",
            description = "Уникальный идентификатор заказа",
            required = true,
            example = "1001"
    )
    @GetMapping
    public ResponseEntity<OrderResponse> getOrder(@PathVariable Long id ){
        return ResponseEntity.ok().body(orderService.getOrderById(id));
    }

    @Operation(
            summary = "Обновление заказа по ID",
            description = "Позволяет обновить информацию о заказе по его уникальному идентификатору",
            responses = {
                    @io.swagger.v3.oas.annotations.responses.ApiResponse(
                            responseCode = "200",
                            description = "Заказ успешно обновлен",
                            content = @io.swagger.v3.oas.annotations.media.Content(
                                    mediaType = "application/json",
                                    schema = @io.swagger.v3.oas.annotations.media.Schema(implementation = OrderResponse.class)
                            )
                    ),
                    @io.swagger.v3.oas.annotations.responses.ApiResponse(
                            responseCode = "404",
                            description = "Заказ с указанным ID не найден",
                            content = @io.swagger.v3.oas.annotations.media.Content(
                                    mediaType = "application/json",
                                    schema = @io.swagger.v3.oas.annotations.media.Schema(implementation = org.example.orderservice.dto.response.ErrorResponse.class)
                            )
                    )
            }
    )
    @Parameters({
            @Parameter(name = "id",
                    description = "Уникальный идентификатор заказа",
                    required = true,
                    example = "1001"
            ),
            @Parameter(
               name = "updateRequest",
               description = "Данные для обновления заказа",
               required = true,
               schema =  @io.swagger.v3.oas.annotations.media.Schema(implementation = OrderUpdateRequest.class)
            )
})
    @PatchMapping
    public ResponseEntity<OrderResponse> updateOrder(@PathVariable Long id, @RequestBody @Valid OrderUpdateRequest updateRequest){
        return ResponseEntity.ok().body(orderService.updateOrder(id, updateRequest));
    }

    @Operation(
            summary = "Удаление заказа по ID",
            description = "Позволяет удалить заказ по его уникальному идентификатору",
            responses = {
                    @io.swagger.v3.oas.annotations.responses.ApiResponse(
                            responseCode = "204",
                            description = "Заказ успешно удален"
                    ),
                    @io.swagger.v3.oas.annotations.responses.ApiResponse(
                            responseCode = "404",
                            description = "Заказ с указанным ID не найден",
                            content = @io.swagger.v3.oas.annotations.media.Content(
                                    mediaType = "application/json",
                                    schema = @io.swagger.v3.oas.annotations.media.Schema(implementation = org.example.orderservice.dto.response.ErrorResponse.class)
                            )
                    )
            }
    )
    @DeleteMapping
    public ResponseEntity<?> deleteOrder(@PathVariable Long id){
        orderService.deleteOrder(id);
        return ResponseEntity.noContent().build();
    }
}
