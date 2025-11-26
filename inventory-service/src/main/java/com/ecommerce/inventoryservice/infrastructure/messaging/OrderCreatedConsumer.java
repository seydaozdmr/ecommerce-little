package com.ecommerce.inventoryservice.infrastructure.messaging;

import com.ecommerce.inventoryservice.application.service.InventoryService;
import com.ecommerce.inventoryservice.domain.event.OrderApproved;
import com.ecommerce.inventoryservice.domain.event.OrderCreatedEvent;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

/**
 * Kafka consumer for OrderCreated events.
 * Processes orders and publishes OrderApproved events.
 */
@Component
@RequiredArgsConstructor
@Slf4j
public class OrderCreatedConsumer {

    private final InventoryService inventoryService;
    private final OrderApprovedPublisher orderApprovedPublisher;

    @KafkaListener(topics = "${spring.kafka.topic.order-events}", groupId = "${spring.kafka.consumer.group-id}", containerFactory = "kafkaListenerContainerFactory")
    public void consume(OrderCreatedEvent event) {
        log.info("Received OrderCreated event: {}", event);

        try {
            // Process the order and check inventory
            boolean approved = inventoryService.processOrder(event);

            // Create and publish OrderApproved event
            OrderApproved orderApproved;
            if (approved) {
                orderApproved = OrderApproved.approved(
                        event.getOrderId(),
                        "Stock available and reserved successfully");
                log.info("Order {} approved", event.getOrderId());
            } else {
                orderApproved = OrderApproved.rejected(
                        event.getOrderId(),
                        "Insufficient stock for one or more items");
                log.warn("Order {} rejected due to insufficient stock", event.getOrderId());
            }

            // Publish the approval event
            orderApprovedPublisher.publish(orderApproved);

        } catch (Exception e) {
            log.error("Error processing OrderCreated event for order {}: {}",
                    event.getOrderId(), e.getMessage(), e);

            // Publish rejection event on error
            OrderApproved rejectedEvent = OrderApproved.rejected(
                    event.getOrderId(),
                    "Error processing order: " + e.getMessage());
            orderApprovedPublisher.publish(rejectedEvent);
        }
    }
}
