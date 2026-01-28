package org.example.apigateway.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.math.BigDecimal;
import java.util.List;

@FeignClient(name = "menu-service", url = "${menu.service.url}")
public interface MenuServiceFeignClient {

    @GetMapping("/pizzas/{id}")
    PizzaResponse getPizza(@PathVariable("id") Long pizzaId);

    @GetMapping("/pizzas")
    List<PizzaResponse> getPizzas();

    record PizzaResponse(Long id,
                         String name,
                         BigDecimal price,
                         Boolean isExists) {
    }
}

