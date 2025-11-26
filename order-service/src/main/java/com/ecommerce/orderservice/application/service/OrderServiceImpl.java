package com.ecommerce.orderservice.application.service;

import com.ecommerce.orderservice.application.dto.CreateOrderRequest;
import com.ecommerce.orderservice.application.dto.OrderItemRequest;
import com.ecommerce.orderservice.application.dto.OrderResponse;
import com.ecommerce.orderservice.domain.event.OrderCreated;
import com.ecommerce.orderservice.domain.exception.OrderException;
import com.ecommerce.orderservice.domain.model.Order;
import com.ecommerce.orderservice.domain.model.OrderItem;
import com.ecommerce.orderservice.domain.repository.OrderRepository;
import com.ecommerce.orderservice.infrastructure.messaging.OrderEventPublisher;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Order Service implementation.
 * Orchestrates order creation and event publishing.
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class OrderServiceImpl implements OrderService {

    private final OrderRepository orderRepository;
    private final OrderEventPublisher eventPublisher;

    @Override
    public OrderResponse createOrder(CreateOrderRequest request) {
        log.info("Creating order for customer: {}", request.getCustomerId());

        try {
            // Convert DTOs to domain objects
            List<OrderItem> orderItems = request.getItems().stream()
                    .map(this::toOrderItem)
                    .collect(Collectors.toList());

            // Create order using domain logic
            Order order = Order.create(request.getCustomerId(), orderItems);

            // Save order
            Order savedOrder = orderRepository.save(order);
            log.info("Order created successfully: {}", savedOrder.getOrderId());

            // Publish domain event to Kafka
            OrderCreated event = OrderCreated.from(
                    savedOrder.getOrderId(),
                    savedOrder.getCustomerId(),
                    savedOrder.getItems(),
                    savedOrder.getTotalAmount(),
                    savedOrder.getCreatedAt());
            eventPublisher.publishOrderCreated(event);
            log.info("OrderCreated event published for order: {}", savedOrder.getOrderId());

            return OrderResponse.from(savedOrder);

        } catch (IllegalArgumentException e) {
            log.error("Invalid order request: {}", e.getMessage());
            throw new OrderException("Invalid order request: " + e.getMessage(), e);
        } catch (Exception e) {
            log.error("Error creating order", e);
            throw new OrderException("Failed to create order", e);
        }
    }

    @Override
    public OrderResponse getOrderById(String orderId) {
        log.info("Fetching order by ID: {}", orderId);
        return orderRepository.findById(orderId)
                .map(OrderResponse::from)
                .orElseThrow(() -> new OrderException("Order not found: " + orderId));
    }

    @Override
    public List<OrderResponse> getAllOrders() {
        log.info("Fetching all orders");
        return orderRepository.findAll().stream()
                .map(OrderResponse::from)
                .collect(Collectors.toList());
    }

    @Override
    public List<OrderResponse> getOrdersByCustomerId(String customerId) {
        log.info("Fetching orders for customer: {}", customerId);
        return orderRepository.findByCustomerId(customerId).stream()
                .map(OrderResponse::from)
                .collect(Collectors.toList());
    }

    private OrderItem toOrderItem(OrderItemRequest request) {
        return OrderItem.create(
                request.getProductId(),
                request.getQuantity(),
                request.getPrice());
    }

}
