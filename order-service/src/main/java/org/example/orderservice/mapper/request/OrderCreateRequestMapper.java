package org.example.orderservice.mapper.request;

import org.mapstruct.Mapper;
import org.mapstruct.Mappings;

@Mapper(componentModel = "spring",
unmappedTargetPolicy = org.mapstruct.ReportingPolicy.IGNORE,
nullValuePropertyMappingStrategy = org.mapstruct.NullValuePropertyMappingStrategy.IGNORE)
public interface OrderCreateRequestMapper {
    @Mappings({
        @org.mapstruct.Mapping(target = "id", ignore = true),
        @org.mapstruct.Mapping(target = "status", expression = "java(org.example.common.kafka.Status.CREATED)"),
        @org.mapstruct.Mapping(target = "createdAt", expression = "java(new java.sql.Timestamp(System.currentTimeMillis()))"),
        @org.mapstruct.Mapping(target = "updatedAt", expression = "java(new java.sql.Timestamp(System.currentTimeMillis()))")
    })
    org.example.orderservice.entity.Order toEntity(org.example.orderservice.dto.request.OrderCreateRequest orderCreateRequest);
}
