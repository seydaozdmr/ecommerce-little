package com.ecommerce.orderservice.infrastructure.messaging;

import com.ecommerce.orderservice.domain.event.OrderApproved;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

/**
 * Consumes order approval events from inventory-service and logs them.
 */
@Component
@Slf4j
public class OrderApprovalConsumer {

    @KafkaListener(topics = "${spring.kafka.topic.order-approval-events}", groupId = "${spring.kafka.consumer.group-id}")
    public void consume(OrderApproved event) {
        log.info("Received OrderApproved event: {}", event);
    }
}
