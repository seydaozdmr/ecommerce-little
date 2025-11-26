package com.ecommerce.inventoryservice.infrastructure.messaging;

import com.ecommerce.inventoryservice.domain.event.OrderApproved;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

/**
 * Publisher for OrderApproved events.
 */
@Component
@RequiredArgsConstructor
@Slf4j
public class OrderApprovedPublisher {

    private final KafkaTemplate<String, Object> kafkaTemplate;

    @Value("${spring.kafka.topic.order-approval-events}")
    private String topic;

    public void publish(OrderApproved event) {
        log.info("Publishing OrderApproved event: {}", event);
        kafkaTemplate.send(topic, event.getOrderId(), event);
        log.info("OrderApproved event published successfully to topic: {}", topic);
    }
}
