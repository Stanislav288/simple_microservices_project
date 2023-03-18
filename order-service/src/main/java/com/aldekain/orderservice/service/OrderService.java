package com.aldekain.orderservice.service;

//import brave.Span;
//import brave.Tracer;
import com.aldekain.orderservice.configuration.RabbitMqProperties;
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
import org.springframework.web.util.UriBuilder;

import java.net.URI;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Transactional
public class OrderService {

    @Value("${INVENTORY_SERVICE_HOSTNAME}")
    private String inventoryServiceHostname;

    @Value("${INVENTORY_SERVICE_PORT}")
    private String inventoryServicePort;

    private final OrderRepository orderRepository;
    private final WebClient.Builder webClientBuilder;
    //private final Tracer tracer;

    private final RabbitTemplate oderNotificationRabbitTemplate;

    private final RabbitMqProperties rabbitMqProperties;

    public String placeOrder(List<OrderItemDto> orderItemList) {
        System.out.println("inventoryServiceHostname="+inventoryServiceHostname);

        System.out.println("inventoryServicePort="+inventoryServicePort);

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

            //FIXME: This will return true even if the product do not exist in inventory
            InventoryResponse[] inventoryResponseArray = webClientBuilder.build().get()
                    .uri( uriBuilder -> uriBuilder.scheme("http")
                            .host(inventoryServiceHostname)
                            .port(inventoryServicePort)
                            .path("/api/inventory")
                            .queryParam("skuCode", skuCodes).build())
//                    .uri("http://"+ inventoryServiceHostname +":"+"/api/inventory",
//                            uriBuilder -> uriBuilder.queryParam("skuCode", skuCodes).build())
                    .retrieve()
                    .bodyToMono(InventoryResponse[].class)
                    .block();

            boolean allProductsInStock = Arrays.stream(inventoryResponseArray)
                    .allMatch(InventoryResponse::isInStock);

        System.out.println("allProductsInStock="+allProductsInStock);

            if (allProductsInStock) {
                System.out.println("BEFORE    orderRepository.save(order);");
                orderRepository.save(order);
                System.out.println("AFTER    orderRepository.save(order);");

                OrderPlacedEvent event = new OrderPlacedEvent(order.getOrderNumber());
                oderNotificationRabbitTemplate.convertAndSend(rabbitMqProperties.getOrderNotificationExchangeName(), "", "Hello from RabbitMQ!");
                //oderInventoryRabbitTemplate.se
               // kafkaTemplate.send("notificationTopic", new OrderPlacedEvent(order.getOrderNumber()));

                return "Order Placed Successfully";
            } else {
                System.out.println("throw new IllegalArgumentException(Product is not in stock, please try again later)");

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
