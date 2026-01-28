package org.example.orderservice.grpc;

import io.grpc.Status;
import io.grpc.StatusRuntimeException;
import lombok.extern.slf4j.Slf4j;
import org.example.common.grpc.pizza.*;
import net.devh.boot.grpc.client.inject.GrpcClient;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
public class PizzaGrpcClient {
    @GrpcClient("menu-service")
    private PizzaServiceGrpc.PizzaServiceBlockingStub pizzaServiceStub;

    public GetPizzasPricesResponse getPizzasPrices(List<Long> ids) {
        log.info("Getting pizzas prices with IDs: {}", ids);

        if (ids == null || ids.isEmpty()) {
            log.warn("Empty pizza IDs list provided for prices");
            throw new IllegalArgumentException("Pizza IDs list cannot be null or empty");
        }

        try {
            GetPizzasPricesRequest request = GetPizzasPricesRequest.newBuilder()
                    .addAllIds(ids)
                    .build();
            GetPizzasPricesResponse response = pizzaServiceStub.getPizzasPrices(request);
            log.info("Retrieved {} prices", response.getPricesCount());
            return response;
        } catch (StatusRuntimeException e) {
            handleGrpcException(e, "gRPC error getting pizzas prices");
            throw new IllegalStateException("This should never happen");
        }
    }

    private void handleGrpcException(StatusRuntimeException e, String message) {
        log.error("{} Status: {}, Description: {}",
                message, e.getStatus().getCode(), e.getStatus().getDescription(), e);

        Status.Code statusCode = e.getStatus().getCode();
        String description = e.getStatus().getDescription();

        switch (statusCode) {
            case UNAVAILABLE:
                throw new RuntimeException("Menu service is unavailable. Please try again later.", e);

            case DEADLINE_EXCEEDED:
                throw new RuntimeException("Menu service timeout. Please try again.", e);

            case INVALID_ARGUMENT:
                throw new IllegalArgumentException("Invalid request: " + description, e);

            case NOT_FOUND:
                throw new IllegalArgumentException("Pizza not found: " + description, e);

            case FAILED_PRECONDITION:
                throw new IllegalStateException("Pizzas are not available: " + description, e);

            case INTERNAL:
                throw new RuntimeException("Internal error in menu service: " + description, e);

            default:
                throw new RuntimeException("Failed to get pizzas prices: " + description, e);
        }
    }
}
