package org.example.orderservice.service.impl;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.example.common.grpc.pizza.GetPizzasPricesResponse;
import org.example.common.grpc.pizza.PizzaPrice;
import org.example.common.kafka.Status;
import org.example.orderservice.dto.request.OrderCreateRequest;
import org.example.orderservice.dto.request.OrderUpdateRequest;
import org.example.orderservice.dto.response.OrderResponse;
import org.example.orderservice.entity.Order;
import org.example.orderservice.exception.NotFoundException;
import org.example.orderservice.grpc.PizzaGrpcClient;
import org.example.orderservice.mapper.request.OrderCreateRequestMapper;
import org.example.orderservice.mapper.request.OrderUpdateRequestMapper;
import org.example.orderservice.mapper.response.OrderResponseMapper;
import org.example.orderservice.repository.OrderRepository;
import org.example.orderservice.service.interfaces.OrderService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.Collection;


@Service
@RequiredArgsConstructor
public class OrderServiceImpl implements OrderService {

    private final OrderRepository orderRepository;
    private final OrderCreateRequestMapper orderCreateRequestMapper;
    private final OrderUpdateRequestMapper orderUpdateRequestMapper;
    private final OrderResponseMapper orderResponseMapper;
    private final PizzaGrpcClient pizzaGrpcClient;

    @Override
    public void deleteOrder(Long id){
        Order order = orderRepository.findById(id).orElseThrow(() -> new NotFoundException("Order not found"));
        orderRepository.delete(order);
    }

    @Override
    public OrderResponse getOrderById(Long id){
        return orderResponseMapper.toDto(orderRepository.findById(id).orElseThrow(() -> new NotFoundException("Order not found")));
    }

    @Transactional
    @Override
    public OrderResponse updateOrder(Long id, OrderUpdateRequest updateRequest){
        Order order = orderRepository.findById(id).orElseThrow(() -> new NotFoundException("Order not found"));
        orderUpdateRequestMapper.updateEntityFromDto(updateRequest, order);
        return orderResponseMapper.toDto(order);
    }

    @Override
    public OrderResponse createOrder(OrderCreateRequest order) {
        GetPizzasPricesResponse pricesResponse = pizzaGrpcClient.getPizzasPrices(order.pizzaIds());

        if (pricesResponse.getPricesCount() != order.pizzaIds().size()) {
            throw new IllegalArgumentException("Some pizzas do not exist");
        }

        BigDecimal totalPrice = pricesResponse.getPricesList()
                .stream()
                .map(PizzaPrice::getPrice)
                .map(BigDecimal::new)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        Order orderEntity = orderCreateRequestMapper.toEntity(order);
        orderEntity.setTotalPrice(totalPrice);
        return orderResponseMapper.toDto(orderRepository.save(orderEntity));
    }

    @Override
    public Page<OrderResponse> getAllOrders(Pageable pageable) {
        return orderRepository.findAll(pageable).map(orderResponseMapper::toDto);
    }

    @Override
    public Page<OrderResponse> getAllOrdersByPizzaId(Collection<Long> pizzaId, Pageable pageable) {
        Page<Order> orders = orderRepository.findDistinctByPizzaIdsIn(
                pizzaId, pageable
        );
        return orders.map(orderResponseMapper::toDto);
    }

    @Transactional
    @Override
    public void updateOrderStatus(Long orderId, Status status) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new NotFoundException("Order not found with id: " + orderId));
        order.setStatus(status);
        orderRepository.save(order);
    }
}
