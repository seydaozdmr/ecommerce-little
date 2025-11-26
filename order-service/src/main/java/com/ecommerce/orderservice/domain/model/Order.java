package com.ecommerce.orderservice.domain.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * Order Aggregate Root in DDD.
 * Represents the main entity for order management.
 * Contains business logic and maintains consistency boundaries.
 */
@Getter
@Builder
@AllArgsConstructor
@ToString
public class Order {

    private final String orderId;
    private final String customerId;
    private final List<OrderItem> items;
    private final BigDecimal totalAmount;
    private final OrderStatus status;
    private final LocalDateTime createdAt;
    private final LocalDateTime updatedAt;

    /**
     * Factory method to create a new Order.
     * Generates order ID and calculates total amount.
     */
    public static Order create(String customerId, List<OrderItem> items) {
        if (items == null || items.isEmpty()) {
            throw new IllegalArgumentException("Order must contain at least one item");
        }

        if (customerId == null || customerId.trim().isEmpty()) {
            throw new IllegalArgumentException("Customer ID cannot be empty");
        }

        BigDecimal totalAmount = items.stream()
                .map(OrderItem::getSubtotal)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        LocalDateTime now = LocalDateTime.now();

        return Order.builder()
                .orderId(UUID.randomUUID().toString())
                .customerId(customerId)
                .items(new ArrayList<>(items))
                .totalAmount(totalAmount)
                .status(OrderStatus.CREATED)
                .createdAt(now)
                .updatedAt(now)
                .build();
    }

    /**
     * Domain method to change order status.
     */
    public Order withStatus(OrderStatus newStatus) {
        return Order.builder()
                .orderId(this.orderId)
                .customerId(this.customerId)
                .items(this.items)
                .totalAmount(this.totalAmount)
                .status(newStatus)
                .createdAt(this.createdAt)
                .updatedAt(LocalDateTime.now())
                .build();
    }
}
