package org.example.orderservice.mapper.response;

import org.example.orderservice.dto.response.OrderResponse;
import org.example.orderservice.entity.Order;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface OrderResponseMapper {
    OrderResponse toDto(Order order);
}
