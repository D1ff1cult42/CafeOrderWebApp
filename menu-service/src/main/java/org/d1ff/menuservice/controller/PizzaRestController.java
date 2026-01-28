package org.d1ff.menuservice.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.Parameters;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.d1ff.menuservice.dto.response.ErrorResponse;
import org.d1ff.menuservice.dto.request.PizzaUpdateRequest;
import org.d1ff.menuservice.dto.response.PizzaResponse;
import org.d1ff.menuservice.service.interfaces.PizzaService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@RestController
@RequestMapping("/pizzas/{id}")
@Tag(name = "Pizza Controller", description = "CRUD операции с пиццами")
public class PizzaRestController {
    private final PizzaService pizzaService;

    @Operation(summary = "Получение пиццы по ID",
            description = "Позволяет получить информацию о пицце по её уникальному идентификатору",
            responses = {
                    @io.swagger.v3.oas.annotations.responses.ApiResponse(
                            responseCode = "200",
                            description = "Пицца успешно получена",
                            content = @io.swagger.v3.oas.annotations.media.Content(
                                    mediaType = "application/json",
                                    schema = @io.swagger.v3.oas.annotations.media.Schema(implementation = PizzaResponse.class)
                            )
                    ),
                    @io.swagger.v3.oas.annotations.responses.ApiResponse(
                            responseCode = "404",
                            description = "Пицца с указанным ID не найдена",
                            content = @Content(
                                    mediaType = "application/json",
                                    schema = @io.swagger.v3.oas.annotations.media.Schema(implementation = ErrorResponse.class)
                            )
                    )
            }
    )
    @Parameter(description = "Уникальный идентификатор пиццы(указывается в пути запроса)", required = true, example = "14")
    @GetMapping
    public ResponseEntity<PizzaResponse> getPizza(@PathVariable Long id){
        return ResponseEntity.ok(pizzaService.getPizzaById(id));
    }

    @Operation(summary = "Удаление пиццы по ID",
            description = "Позволяет удалить пиццу из меню по её уникальному идентификатору(указывается в пути запроса)",
            responses = {
                    @io.swagger.v3.oas.annotations.responses.ApiResponse(
                            responseCode = "204",
                            description = "Пицца успешно удалена"
                    ),
                    @io.swagger.v3.oas.annotations.responses.ApiResponse(
                            responseCode = "404",
                            description = "Пицца с указанным ID не найдена",
                            content = @io.swagger.v3.oas.annotations.media.Content(
                                    mediaType = "application/json",
                                    schema = @io.swagger.v3.oas.annotations.media.Schema(implementation = ErrorResponse.class)
                            )
                    )
            }
    )
    @Parameter(name = "id",
            description = "Уникальный идентификатор пиццы",
            required = true,
            example = "14"
    )
    @DeleteMapping
    public ResponseEntity<?> deletePizza(@PathVariable Long id){
        pizzaService.deletePizza(id);
        return ResponseEntity.noContent().build();
    }

    @Operation(summary = "Обновление информации о пицце",
            description = "Позволяет обновить информацию о пицце по её уникальному идентификатору(указывается в пути запроса)",
            responses = {
                @ApiResponse(
                        responseCode = "200",
                        description = "Информация о пицце успешно обновлена",
                        content = @Content(
                                mediaType = "application/json",
                                schema = @io.swagger.v3.oas.annotations.media.Schema(implementation = PizzaResponse.class)
                        )
                ),
                @ApiResponse(
                        responseCode = "404",
                        description = "Пицца с указанным ID не найдена",
                        content = @io.swagger.v3.oas.annotations.media.Content(
                                mediaType = "application/json",
                                schema = @io.swagger.v3.oas.annotations.media.Schema(implementation = ErrorResponse.class)
                        )
                )
            }
    )
    @Parameters({
            @Parameter(name = "id",
                    description = "Уникальный идентификатор пиццы(указывается в пути запроса)",
                    required = true,
                    example = "14"),
            @Parameter(
                   name = "pizzaUpdateRequest",
                   description = "Данные для обновления информации о пицце",
                   required = true,
                   schema = @io.swagger.v3.oas.annotations.media.Schema(implementation = PizzaUpdateRequest.class)
            )
    })
    @PatchMapping
    public ResponseEntity<PizzaResponse> updatePizza(@PathVariable Long id, @Valid @RequestBody PizzaUpdateRequest pizzaUpdateRequest){
        return ResponseEntity.ok(pizzaService.updatePizza(id, pizzaUpdateRequest));
    }
}
