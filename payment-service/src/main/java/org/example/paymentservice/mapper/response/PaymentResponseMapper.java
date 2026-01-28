package org.example.paymentservice.mapper.response;

import org.example.paymentservice.dto.response.PaymentResponse;
import org.example.paymentservice.entity.Payment;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;

@Mapper(componentModel = "spring")
public interface PaymentResponseMapper {
    @Mapping(source = "yookassaConfirmationUrl", target = "confirmationUrl")
    PaymentResponse toResponse(Payment paymentResponse);
}
