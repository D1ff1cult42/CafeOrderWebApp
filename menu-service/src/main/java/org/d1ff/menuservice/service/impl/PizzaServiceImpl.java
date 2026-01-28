package org.d1ff.menuservice.service.impl;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.d1ff.menuservice.dto.request.PizzaCreateRequest;
import org.d1ff.menuservice.dto.request.PizzaUpdateRequest;
import org.d1ff.menuservice.dto.response.PizzaResponse;
import org.d1ff.menuservice.entity.Pizza;
import org.d1ff.menuservice.exception.ResourceAlreadyExistsException;
import org.d1ff.menuservice.exception.ResourceNotFoundException;
import org.d1ff.menuservice.mapper.request.PizzaAddRequestMapper;
import org.d1ff.menuservice.mapper.request.PizzaUpdateRequestMapper;
import org.d1ff.menuservice.mapper.response.PizzaResponseMapper;
import org.d1ff.menuservice.repository.PizzaRepository;
import org.d1ff.menuservice.service.interfaces.PizzaService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;


@Service
@RequiredArgsConstructor
public class PizzaServiceImpl implements PizzaService {

    private final PizzaRepository pizzaRepository;
    private final PizzaResponseMapper pizzaResponseMapper;
    private final PizzaUpdateRequestMapper pizzaUpdateRequestMapper;
    private final PizzaAddRequestMapper pizzaAddRequestMapper;

    @Value("${exceptions.message.not-found}")
    private String notFoundMessage;

    @Value("${exceptions.message.already-exists}")
    private String alreadyExistsMessage;

    @Override
    public Page<PizzaResponse> getPizzas(Pageable pageable) {
        return pizzaRepository.findAll(pageable)
                .map(pizzaResponseMapper::toDto);
    }

    @Transactional(isolation = Isolation.SERIALIZABLE)
    @Override
    public PizzaResponse addPizza(@Valid PizzaCreateRequest pizza) {
        if (pizzaRepository.findByName(pizza.name()).isPresent()) {
            throw new ResourceAlreadyExistsException(alreadyExistsMessage);
        }
        Pizza newPizza = pizzaRepository.save(pizzaAddRequestMapper.toEntity(pizza));
        return pizzaResponseMapper.toDto(newPizza);
    }

    @Override
    public void deletePizza(Long id) {
        Pizza pizza = pizzaRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException(notFoundMessage));
        pizzaRepository.delete(pizza);
    }

    @Override
    public PizzaResponse getPizzaById(Long id) {
        Pizza pizza = pizzaRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException(notFoundMessage));
        return pizzaResponseMapper.toDto(pizza);
    }

    @Override
    @Transactional
    public PizzaResponse updatePizza(Long id, @Valid PizzaUpdateRequest pizzaUpdateRequest) {
        Pizza pizza = pizzaRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException(notFoundMessage));
        pizzaUpdateRequestMapper.updateEntityFromDto(pizzaUpdateRequest, pizza);
        return pizzaResponseMapper.toDto(pizza);
    }
}

