package com.aldekain.orderservice.configuration;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.amqp.core.*;
import org.springframework.amqp.rabbit.connection.CachingConnectionFactory;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitMqConfig {

    private RabbitMqProperties rabbitMqProperties;

    @Autowired
    public RabbitMqConfig(RabbitMqProperties rabbitMqProperties){
        this.rabbitMqProperties = rabbitMqProperties;
    }

    @Bean
    public MessageConverter jsonMessageConverter() {
        ObjectMapper objectMapper = new ObjectMapper();
        return new Jackson2JsonMessageConverter(objectMapper);
    }

    @Bean
    public ConnectionFactory connectionFactory(RabbitMqProperties rabbitMqProperties) {
        CachingConnectionFactory connectionFactory = new CachingConnectionFactory();
        //connectionFactory.setVirtualHost(virtualHost);
        connectionFactory.setHost(rabbitMqProperties.getHost());
        connectionFactory.setUsername(rabbitMqProperties.getUsername());
        connectionFactory.setPassword(rabbitMqProperties.getPassword());
        return connectionFactory;
    }

    @Bean
    public Queue orderInventoryQueue(RabbitMqProperties rabbitMqProperties) {
        return new Queue(rabbitMqProperties.getOrderinventoryQueueName(), false);
    }

    @Bean
    public TopicExchange orderInventoryExchange(RabbitMqProperties rabbitMqProperties) {
        return new TopicExchange(rabbitMqProperties.getOrderInventoryExchangeName());
    }

    @Bean
    public Binding orderInventoryBinding(Queue orderInventoryQueue, TopicExchange orderInventoryExchange) {
        return BindingBuilder.bind(orderInventoryQueue).to(orderInventoryExchange).with("");
    }

    @Bean
    public RabbitTemplate oderInventoryRabbitTemplate(ConnectionFactory connectionFactory, RabbitMqProperties rabbitMqProperties) {
        final RabbitTemplate rabbitTemplate = new RabbitTemplate(connectionFactory);
          rabbitTemplate.setExchange(rabbitMqProperties.getOrderInventoryExchangeName());
//        rabbitTemplate.setDefaultReceiveQueue(queueName);
//        rabbitTemplate.setMessageConverter(jsonMessageConverter());
//        rabbitTemplate.setReplyAddress(queue().getName());
//        rabbitTemplate.setReplyTimeout(replyTimeout);
//        rabbitTemplate.setUseDirectReplyToContainer(false);
        return rabbitTemplate;
    }
}
