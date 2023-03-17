package com.aldekain.orderservice.service;

//import brave.Span;
//import brave.Tracer;
import com.aldekain.orderservice.event.OrderPlacedEvent;
import com.aldekain.orderservice.model.InventoryResponse;
import com.aldekain.orderservice.model.OrderItemDto;
import com.aldekain.orderservice.entity.Order;
import com.aldekain.orderservice.entity.OrderItem;
import com.aldekain.orderservice.repository.OrderRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.Arrays;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Transactional
public class OrderService {

    private final OrderRepository orderRepository;
    private final WebClient.Builder webClientBuilder;
    //private final Tracer tracer;
    private final KafkaTemplate<String, OrderPlacedEvent> kafkaTemplate;

    private final RabbitTemplate oderInventoryRabbitTemplate;

    @Value()
    private String inventoryServiceName;

    @Value("spring.application.name")
    private String inventoryServiceIp;

    public String placeOrder(List<OrderItemDto> orderItemList) {
        Order order = new Order();

        List<OrderItem> orderItems = orderItemList
                .stream()
                .map(this::mapToDto)
                .toList();

        order.setOrderItemsList(orderItems);

        List<String> skuCodes = order.getOrderItemsList().stream()
                .map(OrderItem::getSkuCode)
                .toList();

//        Span inventoryServiceLookup = tracer.nextSpan().name("InventoryServiceLookup");
//
//        try (Tracer.SpanInScope isLookup = tracer.withSpanInScope(inventoryServiceLookup.start())) {
//
//            inventoryServiceLookup.tag("call", "inventory-service");
            // Call Inventory Service, and place order if product is in
            // stock
            InventoryResponse[] inventoryResponseArray = webClientBuilder.build().get()
                    .uri("http://inventory-service:49184/api/inventory",
                            uriBuilder -> uriBuilder.queryParam("skuCode", skuCodes).build())
                    .retrieve()
                    .bodyToMono(InventoryResponse[].class)
                    .block();

            boolean allProductsInStock = Arrays.stream(inventoryResponseArray)
                    .allMatch(InventoryResponse::isInStock);

            if (allProductsInStock) {
                orderRepository.save(order);
                OrderPlacedEvent event = new OrderPlacedEvent(order.getOrderNumber());
                oderInventoryRabbitTemplate.convertAndSend(event);
                //oderInventoryRabbitTemplate.se
               // kafkaTemplate.send("notificationTopic", new OrderPlacedEvent(order.getOrderNumber()));

                return "Order Placed Successfully";
            } else {
                throw new IllegalArgumentException("Product is not in stock, please try again later");
            }
//        } finally {
//            inventoryServiceLookup.flush();
//        }
    }

    private OrderItem mapToDto(OrderItemDto orderItemDto) {
        OrderItem orderItem = new OrderItem();
        orderItem.setOrderItemId(orderItem.getOrderItemId());
        orderItem.setPrice(orderItemDto.getPrice());
        orderItem.setQuantity(orderItemDto.getQuantity());
        orderItem.setSkuCode(orderItemDto.getSkuCode());
        return orderItem;
    }
}
