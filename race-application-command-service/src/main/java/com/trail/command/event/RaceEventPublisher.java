package com.trail.command.event;

import lombok.RequiredArgsConstructor;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class RaceEventPublisher {

    private final RabbitTemplate rabbitTemplate;

    @Value("${rabbitmq.exchange:race.exchange}")
    private String exchange;

    @Value("${rabbitmq.routing-key:race.created}")
    private String createdRoutingKey;

    @Value("${rabbitmq.routing-key-deleted:race.deleted}")
    private String deletedRoutingKey;

    @Value("${rabbitmq.routing-key-updated:race.updated}")
    private String updatedRoutingKey;

    @Value("${rabbitmq.routing-key-application-created:application.created}")
    private String applicationCreatedKey;

    @Value("${rabbitmq.routing-key-application-deleted:application.deleted}")
    private String applicationDeletedKey;

    public void publishApplicationDeleted(ApplicationDeletedEvent event) {
        rabbitTemplate.convertAndSend(exchange, applicationDeletedKey, event);
    }

    public void publishRaceCreated(RaceCreatedEvent event) {
        rabbitTemplate.convertAndSend(exchange, createdRoutingKey, event);
    }

    public void publishRaceDeleted(RaceDeletedEvent event) {
        rabbitTemplate.convertAndSend(exchange, deletedRoutingKey, event);
    }

    public void publishRaceUpdated(RaceUpdatedEvent event) {
        rabbitTemplate.convertAndSend(exchange, updatedRoutingKey, event);
    }
    public void publishApplicationCreated(ApplicationCreatedEvent event) {
        rabbitTemplate.convertAndSend(exchange, applicationCreatedKey, event);
    }
}
