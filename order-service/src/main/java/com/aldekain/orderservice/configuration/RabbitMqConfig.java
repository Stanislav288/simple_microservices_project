package com.aldekain.orderservice.configuration;

import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.TopicExchange;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.listener.SimpleMessageListenerContainer;
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

//    @Bean
//    public MessageConverter jsonMessageConverter() {
//        ObjectMapper objectMapper = new ObjectMapper();
//        return new Jackson2JsonMessageConverter(objectMapper);
//    }

//    @Bean
//    public ConnectionFactory connectionFactory(RabbitMqProperties rabbitMqProperties) {
//        CachingConnectionFactory connectionFactory = new CachingConnectionFactory();
//        //connectionFactory.setVirtualHost(virtualHost);
//        connectionFactory.setHost(rabbitMqProperties.getHost());
//        connectionFactory.setUsername(rabbitMqProperties.getUsername());
//        connectionFactory.setPassword(rabbitMqProperties.getPassword());
//        return connectionFactory;
//    }

    @Bean
    public Queue orderNotificationQueue(RabbitMqProperties rabbitMqProperties) {
        return new Queue(rabbitMqProperties.getOrderNotificationQueueName(), false);
    }

    @Bean
    public TopicExchange orderNotificationExchange(RabbitMqProperties rabbitMqProperties) {
        return new TopicExchange(rabbitMqProperties.getOrderNotificationExchangeName());
    }

    @Bean
    public Binding orderNotificationBinding(Queue orderNotificationQueue, TopicExchange orderNotificationExchange) {
        return BindingBuilder.bind(orderNotificationQueue).to(orderNotificationExchange).with("");
    }

    @Bean
    public SimpleMessageListenerContainer container(ConnectionFactory connectionFactory) {
        SimpleMessageListenerContainer container = new SimpleMessageListenerContainer();
        container.setConnectionFactory(connectionFactory);
        return container;
    }

//    @Bean
//    public RabbitTemplate oderNotificationRabbitTemplate(ConnectionFactory connectionFactory, RabbitMqProperties rabbitMqProperties) {
//        final RabbitTemplate rabbitTemplate = new RabbitTemplate(connectionFactory);
//          rabbitTemplate.setExchange(rabbitMqProperties.getOrderInventoryExchangeName());
////        rabbitTemplate.setDefaultReceiveQueue(queueName);
////        rabbitTemplate.setMessageConverter(jsonMessageConverter());
////        rabbitTemplate.setReplyAddress(queue().getName());
////        rabbitTemplate.setReplyTimeout(replyTimeout);
////        rabbitTemplate.setUseDirectReplyToContainer(false);
//        return rabbitTemplate;
//    }

//    @Bean
//    public SimpleMessageListenerContainer container(ConnectionFactory connectionFactory,
//                                                    MessageListenerAdapter listenerAdapter,
//                                                    RabbitMqProperties rabbitMqProperties) {
//        SimpleMessageListenerContainer container = new SimpleMessageListenerContainer();
//        container.setConnectionFactory(connectionFactory);
//        container.setQueueNames(rabbitMqProperties.getOrderinventoryQueueName());
//        container.setMessageListener(listenerAdapter);
//        return container;
//    }
//
//    @Bean
//    public MessageListenerAdapter listenerAdapter(Receiver receiver) {
//        return new MessageListenerAdapter(receiver, "receiveMessage");
//    }
}
