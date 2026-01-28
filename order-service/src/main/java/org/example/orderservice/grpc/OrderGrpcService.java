package org.example.orderservice.grpc;

import io.grpc.stub.StreamObserver;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.devh.boot.grpc.server.service.GrpcService;
import org.example.common.grpc.order.GetOrderPriceRequest;
import org.example.common.grpc.order.GetOrderPriceResponse;
import org.example.common.grpc.order.OrderServiceGrpc;
import org.example.orderservice.entity.Order;
import org.example.orderservice.repository.OrderRepository;

@Slf4j
@GrpcService
@RequiredArgsConstructor
public class OrderGrpcService extends OrderServiceGrpc.OrderServiceImplBase {
    private final OrderRepository orderRepository;

    @Override
    public void getOrderPrice(GetOrderPriceRequest request, StreamObserver<GetOrderPriceResponse> responseObserver){
        try{
            log.info("gRPC getOrderPrice called with orderId: {}", request.getOrderId());
            if(request.getOrderId() < 0){
                log.warn("Invalid order ID received: {}", request.getOrderId());
                responseObserver.onError(io.grpc.Status.INVALID_ARGUMENT
                        .withDescription("Order ID must be a positive number and cannot be null")
                        .asRuntimeException());
                return;
            }
            Long orderId = request.getOrderId();
            Order order = orderRepository.findById(orderId).orElse(null);
            if (order == null){
                log.warn("Order not found with ID: {}", orderId);
                responseObserver.onError(io.grpc.Status.NOT_FOUND
                        .withDescription("Order not found with ID: " + orderId)
                        .asRuntimeException());
                return;
            }

            GetOrderPriceResponse response = GetOrderPriceResponse.newBuilder()
                    .setId(order.getId())
                    .setTotalPrice(order.getTotalPrice().toString())
                    .build();

            log.info("Successfully retrieved order price for orderId: {}", orderId);

            responseObserver.onNext(response);
            responseObserver.onCompleted();
        }catch(Exception e){
            log.error("Error processing gRPC getOrderPrice request", e);
            responseObserver.onError(io.grpc.Status.INTERNAL
                    .withDescription("Internal server error")
                    .asRuntimeException());
        }
    }
}
