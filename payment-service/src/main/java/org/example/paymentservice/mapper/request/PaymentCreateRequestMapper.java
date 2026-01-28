package org.example.paymentservice.mapper.request;

import org.example.paymentservice.dto.request.PaymentCreateRequest;
import org.example.paymentservice.entity.Payment;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface PaymentCreateRequestMapper {
    void toPaymentFromRequest(PaymentCreateRequest paymentRequest, @MappingTarget Payment payment);
}
