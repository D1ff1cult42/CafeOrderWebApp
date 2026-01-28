package org.example.apigateway.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.apigateway.client.MenuServiceFeignClient;
import org.example.apigateway.client.OrderServiceFeignClient;
import org.example.apigateway.client.PaymentServiceFeignClient;
import org.example.apigateway.dto.AggregatedOrderResponse;
import org.example.apigateway.dto.AggregatedPaymentResponse;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@RequiredArgsConstructor
@Service
@Slf4j
public class AggregationService {
    private final ExecutorService executor = Executors.newFixedThreadPool(10);
    private final MenuServiceFeignClient menuServiceFeignClient;
    private final OrderServiceFeignClient orderServiceFeignClient;
    private final PaymentServiceFeignClient paymentServiceFeignClient;

    public AggregatedOrderResponse getOrderWithPizzas(Long orderId) {
        log.info("Aggregating data for orderId: {}", orderId);

        OrderServiceFeignClient.OrderResponse orderResponse = orderServiceFeignClient.getOrder(orderId);

        List<CompletableFuture<MenuServiceFeignClient.PizzaResponse>> pizzaFutures =
                orderResponse.pizzaIds().stream()
                        .map(pizzaId -> CompletableFuture.supplyAsync(() ->
                                menuServiceFeignClient.getPizza(pizzaId), executor))
                        .toList();

        List<AggregatedOrderResponse.PizzaInfo> pizzas = pizzaFutures.stream()
                .map(CompletableFuture::join)
                .map(pizza -> new AggregatedOrderResponse.PizzaInfo(
                        pizza.id(),
                        pizza.name(),
                        pizza.price(),
                        pizza.isExists()))
                .toList();
        return new AggregatedOrderResponse(
                orderResponse.id(),
                orderResponse.name(),
                orderResponse.address(),
                orderResponse.phone(),
                orderResponse.status(),
                orderResponse.totalPrice(),
                pizzas,
                orderResponse.createdAt(),
                orderResponse.updatedAt()
        );
    }

    public AggregatedPaymentResponse getPaymentWithOrder(Long paymentId) {
        log.info("Aggregating data for paymentId: {}", paymentId);

        PaymentServiceFeignClient.PaymentResponse paymentResponse = paymentServiceFeignClient.getPayment(paymentId);

        CompletableFuture<OrderServiceFeignClient.OrderResponse> orderFuture =
                CompletableFuture.supplyAsync(() ->
                        orderServiceFeignClient.getOrder(paymentResponse.orderId()), executor);

        OrderServiceFeignClient.OrderResponse orderResponse = orderFuture.join();

        AggregatedPaymentResponse.OrderInfo orderInfo = new AggregatedPaymentResponse.OrderInfo(
                orderResponse.id(),
                orderResponse.name(),
                orderResponse.address(),
                orderResponse.phone(),
                orderResponse.status(),
                orderResponse.totalPrice(),
                orderResponse.createdAt(),
                orderResponse.updatedAt()
        );

        return new AggregatedPaymentResponse(
                paymentResponse.id(),
                paymentResponse.amount(),
                paymentResponse.status(),
                paymentResponse.yookassaPaymentId(),
                paymentResponse.confirmationUrl(),
                paymentResponse.createdAt(),
                paymentResponse.updatedAt(),
                orderInfo
        );
    }
}
