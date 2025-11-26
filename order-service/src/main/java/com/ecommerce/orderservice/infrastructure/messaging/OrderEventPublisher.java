package com.ecommerce.orderservice.infrastructure.messaging;

import com.ecommerce.orderservice.domain.event.OrderCreated;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;
import org.springframework.stereotype.Component;

import java.util.concurrent.CompletableFuture;

/**
 * Publisher for order domain events to Kafka.
 * Infrastructure component that handles event publishing.
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class OrderEventPublisher {

    private final KafkaTemplate<String, Object> kafkaTemplate;

    @Value("${spring.kafka.topic.order-events}")
    private String orderEventsTopic;

    /**
     * Publish OrderCreated event to Kafka.
     */
    public void publishOrderCreated(OrderCreated event) {
        log.info("Publishing OrderCreated event to Kafka: {}", event.getOrderId());

        CompletableFuture<SendResult<String, Object>> future = kafkaTemplate.send(orderEventsTopic, event.getOrderId(),
                event);

        future.whenComplete((result, ex) -> {
            if (ex == null) {
                log.info("Successfully published OrderCreated event for order: {} to topic: {}",
                        event.getOrderId(), orderEventsTopic);
            } else {
                log.error("Failed to publish OrderCreated event for order: {}",
                        event.getOrderId(), ex);
            }
        });
    }
}
