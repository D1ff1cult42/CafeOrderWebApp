package org.d1ff.menuservice.grpc;

import io.grpc.Status;
import io.grpc.stub.StreamObserver;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.devh.boot.grpc.server.service.GrpcService;
import org.d1ff.menuservice.entity.Pizza;
import org.example.common.grpc.pizza.*;
import org.d1ff.menuservice.repository.PizzaRepository;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;


@Slf4j
@RequiredArgsConstructor
@GrpcService
public class PizzaGrpcService extends PizzaServiceGrpc.PizzaServiceImplBase {
    private final PizzaRepository pizzaRepository;

    @Override
    public void getPizzasPrices(GetPizzasPricesRequest request, StreamObserver<GetPizzasPricesResponse> responseObserver) {
        try {
            log.info("gRPC getPizzasPrices called with pizzaIds: {}", request.getIdsList());

            if (request.getIdsList().isEmpty()) {
                log.warn("Empty pizza IDs list received");
                responseObserver.onError(Status.INVALID_ARGUMENT
                        .withDescription("Pizza IDs list cannot be empty")
                        .asRuntimeException());
                return;
            }

            List<Long> requestedIds = request.getIdsList();

            List<Pizza> foundPizzas = pizzaRepository.findAllById(requestedIds);

            Set<Long> foundIds = foundPizzas.stream()
                    .map(Pizza::getId)
                    .collect(Collectors.toSet());

            Set<Long> requestedIdsSet = new HashSet<>(requestedIds);
            requestedIdsSet.removeAll(foundIds);

            if (!requestedIdsSet.isEmpty()) {
                log.warn("Pizza IDs not found: {}", requestedIdsSet);
                responseObserver.onError(Status.NOT_FOUND
                        .withDescription("Pizza IDs not found: " + requestedIdsSet)
                        .asRuntimeException());
                return;
            }

            List<Pizza> nonExistentPizzas = foundPizzas.stream()
                    .filter(pizza -> !Boolean.TRUE.equals(pizza.getIsExists()))
                    .toList();

            if (!nonExistentPizzas.isEmpty()) {
                List<Long> nonExistentIds = nonExistentPizzas.stream()
                        .map(Pizza::getId)
                        .toList();
                log.warn("Pizza IDs marked as non-existent: {}", nonExistentIds);
                responseObserver.onError(Status.FAILED_PRECONDITION
                        .withDescription("Pizzas are not available: " + nonExistentIds)
                        .asRuntimeException());
                return;
            }

            GetPizzasPricesResponse.Builder responseBuilder = GetPizzasPricesResponse.newBuilder();

            foundPizzas.forEach(pizza -> {
                PizzaPrice pizzaPrice = PizzaPrice.newBuilder()
                        .setId(pizza.getId())
                        .setPrice(pizza.getPrice().toString())
                        .build();
                responseBuilder.addPrices(pizzaPrice);
            });

            GetPizzasPricesResponse response = responseBuilder.build();
            log.info("Returning {} pizza prices", response.getPricesCount());

            responseObserver.onNext(response);
            responseObserver.onCompleted();

        } catch (Exception e) {
            log.error("Error getting pizzas prices", e);
            responseObserver.onError(Status.INTERNAL
                    .withDescription("Failed to get pizzas prices: " + e.getMessage())
                    .withCause(e)
                    .asRuntimeException());
        }
    }
}