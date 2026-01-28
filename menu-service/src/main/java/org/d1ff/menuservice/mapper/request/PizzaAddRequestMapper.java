package org.d1ff.menuservice.mapper.request;

import org.d1ff.menuservice.dto.request.PizzaCreateRequest;
import org.d1ff.menuservice.entity.Pizza;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring",
        unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface PizzaAddRequestMapper {
    @Mappings({
            @Mapping(target = "id", ignore = true),
            @Mapping(target = "isExists", expression = "java(true)")
    })

    Pizza toEntity(PizzaCreateRequest pizzaCreateRequest);
}
