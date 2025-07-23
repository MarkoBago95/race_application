#!/bin/bash

# Wait for RabbitMQ to start
sleep 30

# Create exchange and queues
rabbitmqadmin declare exchange name=race.exchange type=topic
rabbitmqadmin declare queue name=race.created.queue durable=true
rabbitmqadmin declare queue name=race.updated.queue durable=true
rabbitmqadmin declare queue name=race.deleted.queue durable=true
rabbitmqadmin declare queue name=application.deleted.queue durable=true

# Create bindings
rabbitmqadmin declare binding source=race.exchange destination=race.created.queue routing_key=race.created
rabbitmqadmin declare binding source=race.exchange destination=race.updated.queue routing_key=race.updated
rabbitmqadmin declare binding source=race.exchange destination=race.deleted.queue routing_key=race.deleted
rabbitmqadmin declare binding source=race.exchange destination=application.deleted.queue routing_key=application.deleted

echo "RabbitMQ queues and bindings created!"