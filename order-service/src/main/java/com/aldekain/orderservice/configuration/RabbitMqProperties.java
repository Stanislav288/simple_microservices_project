package com.aldekain.orderservice.configuration;

import jakarta.annotation.PostConstruct;
import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties(prefix = "spring.rabbitmq")
@Getter
@Setter
public class RabbitMqProperties {

    /**
     * This will not work, the whole variable should be specified as @ConfigurationProperties(prefix) does not affect @Value
     * @Value("${ordernotificationqueuename}")
     */
    private String orderNotificationQueueName;

    private String orderNotificationExchangeName;

    private String host;

    private int port;

    private String username;

    private String password;
}
