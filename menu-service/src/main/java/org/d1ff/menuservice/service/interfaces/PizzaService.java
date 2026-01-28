package org.d1ff.menuservice.service.interfaces;

import jakarta.validation.Valid;
import org.d1ff.menuservice.dto.request.PizzaCreateRequest;
import org.d1ff.menuservice.dto.request.PizzaUpdateRequest;
import org.d1ff.menuservice.dto.response.PizzaResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;


public interface PizzaService {
    Page<PizzaResponse> getPizzas(Pageable pageable);
    PizzaResponse addPizza(@Valid PizzaCreateRequest pizza);
    void deletePizza(Long id);
    PizzaResponse getPizzaById(Long id);
    PizzaResponse updatePizza(Long id, @Valid PizzaUpdateRequest pizzaUpdateRequest);
}
