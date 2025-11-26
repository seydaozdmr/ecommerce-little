package com.ecommerce.orderservice.domain.repository;

import com.ecommerce.orderservice.domain.model.Order;

import java.util.List;
import java.util.Optional;

/**
 * Repository interface for Order aggregate.
 * Following DDD repository pattern - defines contract for persistence.
 */
public interface OrderRepository {

    /**
     * Save an order.
     */
    Order save(Order order);

    /**
     * Find order by ID.
     */
    Optional<Order> findById(String orderId);

    /**
     * Find all orders.
     */
    List<Order> findAll();

    /**
     * Find orders by customer ID.
     */
    List<Order> findByCustomerId(String customerId);
}
