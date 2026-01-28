package org.d1ff.menuservice.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.d1ff.menuservice.dto.response.ErrorResponse;
import org.d1ff.menuservice.dto.request.PizzaCreateRequest;
import org.d1ff.menuservice.dto.response.PizzaResponse;
import org.d1ff.menuservice.service.interfaces.PizzaService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.net.URI;
import org.example.common.pageGenerics.PageResponse;

@RestController
@RequiredArgsConstructor
@Tag(name = "Pizzas Rest Controller", description = "Операции с пиццами")
@RequestMapping("/pizzas")
public class PizzasRestController {
    private final PizzaService pizzaService;

    @Operation(
            summary = "Получение всех пицц с пагинацией",
            description = "Позволяет получить список всех пицц с возможностью пагинации",
            responses = {
                    @io.swagger.v3.oas.annotations.responses.ApiResponse(
                            responseCode = "200",
                            description = "Список пицц успешно получен",
                            content = @io.swagger.v3.oas.annotations.media.Content(
                                    mediaType = "application/json",
                                    schema = @io.swagger.v3.oas.annotations.media.Schema(implementation = PageResponse.class)
                            )
                    )
            }
    )
    @Parameter(name = "page", description = "Номер страницы для пагинации", example = "0")
    @Parameter(name = "size", description = "Количество элементов на странице", example = "10")
    @GetMapping
    public ResponseEntity<PageResponse<PizzaResponse>> getAllPizzas(@RequestParam(defaultValue = "0") int page,
                                                                    @RequestParam(defaultValue = "10")int size) {
        Pageable pageable = PageRequest.of(page,size);
        Page<PizzaResponse> pizzas = pizzaService.getPizzas(pageable);
        return ResponseEntity.ok(PageResponse.fromPage(pizzas));
    }

    @Operation(
            summary = "Добавление новой пиццы",
            description = "Позволяет добавить новую пиццу в меню",
            responses = {
                    @io.swagger.v3.oas.annotations.responses.ApiResponse(
                            responseCode = "201",
                            description = "Пицца успешно добавлена",
                            content = @io.swagger.v3.oas.annotations.media.Content(
                                    mediaType = "application/json",
                                    schema = @io.swagger.v3.oas.annotations.media.Schema(implementation = PizzaResponse.class)
                            )
                    ),
                    @io.swagger.v3.oas.annotations.responses.ApiResponse(
                            responseCode = "409",
                            description = "Пицца с таким именем уже существует",
                            content = @io.swagger.v3.oas.annotations.media.Content(
                                    mediaType = "application/json",
                                    schema = @io.swagger.v3.oas.annotations.media.Schema(implementation = ErrorResponse.class))
                    )
            }
    )
    @Parameter(
            name = "pizzaCreateRequest",
            description = "Данные для создания новой пиццы",
            required = true,
            schema = @io.swagger.v3.oas.annotations.media.Schema(implementation = PizzaCreateRequest.class)
    )
    @PostMapping
    public ResponseEntity<PizzaResponse> addPizza(@Valid @RequestBody PizzaCreateRequest pizzaCreateRequest){
        PizzaResponse pizzaResponse = pizzaService.addPizza(pizzaCreateRequest);
        return ResponseEntity.created(URI.create("/pizzas/" + pizzaResponse.id()))
                .body(pizzaResponse);
    }
}
