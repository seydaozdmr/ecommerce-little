package com.ecommerce.orderservice.domain.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

import java.math.BigDecimal;

/**
 * OrderItem Value Object in DDD.
 * Represents an item within an order.
 * Immutable by design.
 */
@Getter
@Builder
@AllArgsConstructor
@ToString
public class OrderItem {

    private final String productId;
    private final Integer quantity;
    private final BigDecimal price;
    private final BigDecimal subtotal;

    /**
     * Factory method to create an OrderItem with calculated subtotal.
     */
    public static OrderItem create(String productId, Integer quantity, BigDecimal price) {
        BigDecimal subtotal = price.multiply(BigDecimal.valueOf(quantity));
        return OrderItem.builder()
                .productId(productId)
                .quantity(quantity)
                .price(price)
                .subtotal(subtotal)
                .build();
    }
}
