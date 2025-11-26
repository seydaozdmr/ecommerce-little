package com.ecommerce.orderservice.domain.model;

/**
 * Order status enum representing the lifecycle of an order.
 * Part of the domain model in DDD.
 */
public enum OrderStatus {
    CREATED,
    CONFIRMED,
    PROCESSING,
    SHIPPED,
    DELIVERED,
    CANCELLED
}
