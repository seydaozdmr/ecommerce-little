package com.ecommerce.orderservice.domain.event;

import com.ecommerce.orderservice.domain.model.OrderItem;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

/**
 * OrderCreated Domain Event.
 * Published when a new order is created.
 * This event will be sent to Kafka.
 */
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class OrderCreated {

    private String orderId;
    private String customerId;
    private List<OrderItem> items;
    private BigDecimal totalAmount;
    private LocalDateTime createdAt;
    private String eventType;

    public static OrderCreated from(String orderId, String customerId, List<OrderItem> items,
            BigDecimal totalAmount, LocalDateTime createdAt) {
        return OrderCreated.builder()
                .orderId(orderId)
                .customerId(customerId)
                .items(items)
                .totalAmount(totalAmount)
                .createdAt(createdAt)
                .eventType("ORDER_CREATED")
                .build();
    }
}
