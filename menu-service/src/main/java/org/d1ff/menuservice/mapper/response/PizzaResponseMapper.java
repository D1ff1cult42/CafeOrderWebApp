package org.d1ff.menuservice.mapper.response;

import org.d1ff.menuservice.dto.response.PizzaResponse;
import org.d1ff.menuservice.entity.Pizza;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring",
        unmappedTargetPolicy = ReportingPolicy.IGNORE
)
public interface PizzaResponseMapper {
    PizzaResponse toDto(Pizza pizza);
}
