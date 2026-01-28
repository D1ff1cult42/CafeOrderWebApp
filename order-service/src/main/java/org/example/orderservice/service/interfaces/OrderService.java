package org.example.orderservice.service.interfaces;

import org.example.common.kafka.Status;
import org.example.orderservice.dto.request.OrderCreateRequest;
import org.example.orderservice.dto.request.OrderUpdateRequest;
import org.example.orderservice.dto.response.OrderResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Collection;

public interface OrderService {

    void deleteOrder(Long id);

    OrderResponse updateOrder(Long id, OrderUpdateRequest order);

    OrderResponse getOrderById(Long id);

    OrderResponse createOrder(OrderCreateRequest order);

    Page<OrderResponse> getAllOrders(Pageable pageable);

    Page<OrderResponse> getAllOrdersByPizzaId(Collection<Long> pizzaId, Pageable pageable);

    void updateOrderStatus(Long orderId, Status status);
}