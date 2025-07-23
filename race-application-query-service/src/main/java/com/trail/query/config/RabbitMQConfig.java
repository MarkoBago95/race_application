package com.trail.query.config;

import org.springframework.amqp.core.*;
import org.springframework.amqp.rabbit.annotation.RabbitListenerConfigurer;
import org.springframework.amqp.rabbit.listener.RabbitListenerEndpointRegistrar;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.converter.MappingJackson2MessageConverter;
import org.springframework.messaging.handler.annotation.support.DefaultMessageHandlerMethodFactory;
import org.springframework.messaging.handler.annotation.support.MessageHandlerMethodFactory;

@Configuration
public class RabbitMQConfig implements RabbitListenerConfigurer {

    @Value("${rabbitmq.exchange}")
    private String exchange;

    @Value("${rabbitmq.queue-created}")
    private String queueCreated;

    @Value("${rabbitmq.queue-updated}")
    private String queueUpdated;

    @Value("${rabbitmq.queue-deleted}")
    private String queueDeleted;

    @Value("${rabbitmq.queue-application-deleted}")
    private String queueApplicationDeleted;

    @Value("${rabbitmq.routing-key-created}")
    private String routingKeyCreated;

    @Value("${rabbitmq.routing-key-updated}")
    private String routingKeyUpdated;

    @Value("${rabbitmq.routing-key-deleted}")
    private String routingKeyDeleted;

    @Value("${rabbitmq.routing-key-application-deleted}")
    private String routingKeyApplicationDeleted;

    // JSON Message Converter
    @Bean
    public MessageConverter jsonMessageConverter() {
        return new Jackson2JsonMessageConverter();
    }

    // Message Handler Factory za RabbitListener
    @Bean
    public MessageHandlerMethodFactory messageHandlerMethodFactory() {
        DefaultMessageHandlerMethodFactory factory = new DefaultMessageHandlerMethodFactory();
        factory.setMessageConverter(new MappingJackson2MessageConverter());
        return factory;
    }

    @Override
    public void configureRabbitListeners(RabbitListenerEndpointRegistrar registrar) {
        registrar.setMessageHandlerMethodFactory(messageHandlerMethodFactory());
    }

    // Exchange
    @Bean
    public TopicExchange exchange() {
        return new TopicExchange(exchange);
    }

    // Queues - Spring će automatski kreirati
    @Bean
    public Queue queueCreated() {
        return QueueBuilder.durable(queueCreated).build();
    }

    @Bean
    public Queue queueUpdated() {
        return QueueBuilder.durable(queueUpdated).build();
    }

    @Bean
    public Queue queueDeleted() {
        return QueueBuilder.durable(queueDeleted).build();
    }

    @Bean
    public Queue queueApplicationDeleted() {
        return QueueBuilder.durable(queueApplicationDeleted).build();
    }

    // Bindings - Spring će automatski kreirati
    @Bean
    public Binding bindingCreated() {
        return BindingBuilder.bind(queueCreated()).to(exchange()).with(routingKeyCreated);
    }

    @Bean
    public Binding bindingUpdated() {
        return BindingBuilder.bind(queueUpdated()).to(exchange()).with(routingKeyUpdated);
    }

    @Bean
    public Binding bindingDeleted() {
        return BindingBuilder.bind(queueDeleted()).to(exchange()).with(routingKeyDeleted);
    }

    @Bean
    public Binding bindingApplicationDeleted() {
        return BindingBuilder.bind(queueApplicationDeleted()).to(exchange()).with(routingKeyApplicationDeleted);
    }
}