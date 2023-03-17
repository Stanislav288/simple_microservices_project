package com.aldekain.inventoryservice.rabbitmq;

import org.springframework.stereotype.Component;

@Component
public class RabbitMqReceiver {
    public void receiveMessage(String message) {
        System.out.println("Received <" + message + ">");
    }
}
