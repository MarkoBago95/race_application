package com.trail.command.config;

import org.springframework.amqp.core.*;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitMQConfig {

    @Value("${rabbitmq.exchange}")
    private String exchange;

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

    // RabbitTemplate sa JSON converter
    @Bean
    public RabbitTemplate rabbitTemplate(ConnectionFactory connectionFactory) {
        RabbitTemplate template = new RabbitTemplate(connectionFactory);
        template.setMessageConverter(jsonMessageConverter());
        return template;
    }

    // Exchange - samo exchange za command service
    @Bean
    public TopicExchange exchange() {
        return new TopicExchange(exchange);
    }

    // Command service NE KREIRA queue-ove - samo šalje poruke!
    // Query service će kreirati queue-ove i slušati
}