package com.ecommerce.orderservice.application.service;

import com.ecommerce.orderservice.application.dto.CreateOrderRequest;
import com.ecommerce.orderservice.application.dto.OrderResponse;

import java.util.List;

/**
 * Order Service interface.
 * Defines application-level operations for order management.
 */
public interface OrderService {

    /**
     * Create a new order.
     */
    OrderResponse createOrder(CreateOrderRequest request);

    /**
     * Get order by ID.
     */
    OrderResponse getOrderById(String orderId);

    /**
     * Get all orders.
     */
    List<OrderResponse> getAllOrders();

    /**
     * Get orders by customer ID.
     */
    List<OrderResponse> getOrdersByCustomerId(String customerId);
}
