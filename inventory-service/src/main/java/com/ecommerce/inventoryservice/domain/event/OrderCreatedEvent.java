package com.ecommerce.inventoryservice.domain.event;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

/**
 * OrderCreatedEvent - consumed from Kafka.
 * Represents an order created event from order-service.
 */
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class OrderCreatedEvent {

    private String orderId;
    private String customerId;
    private List<OrderItemEvent> items;
    private BigDecimal totalAmount;
    private LocalDateTime createdAt;
    private String eventType;

    @Getter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    @ToString
    public static class OrderItemEvent {
        private String productId;
        private Integer quantity;
        private BigDecimal price;
    }
}
