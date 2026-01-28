package org.d1ff.menuservice.mapper.request;

import org.d1ff.menuservice.dto.request.PizzaUpdateRequest;
import org.d1ff.menuservice.entity.Pizza;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;

@Mapper(componentModel = "spring",
        unmappedTargetPolicy = org.mapstruct.ReportingPolicy.IGNORE,
        nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
public interface PizzaUpdateRequestMapper {
    @Mapping(target = "id", ignore = true)
    Pizza updateEntityFromDto(PizzaUpdateRequest pizzaUpdateRequest, @MappingTarget Pizza pizza);
}
