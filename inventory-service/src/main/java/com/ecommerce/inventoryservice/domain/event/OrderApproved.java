package com.ecommerce.inventoryservice.domain.event;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.time.LocalDateTime;

/**
 * OrderApproved Domain Event.
 * Published to Kafka after processing an order.
 */
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class OrderApproved {

    private String orderId;
    private Boolean approved;
    private String reason;
    private LocalDateTime processedAt;
    private String eventType;

    public static OrderApproved approved(String orderId, String reason) {
        return OrderApproved.builder()
                .orderId(orderId)
                .approved(true)
                .reason(reason)
                .processedAt(LocalDateTime.now())
                .eventType("ORDER_APPROVED")
                .build();
    }

    public static OrderApproved rejected(String orderId, String reason) {
        return OrderApproved.builder()
                .orderId(orderId)
                .approved(false)
                .reason(reason)
                .processedAt(LocalDateTime.now())
                .eventType("ORDER_APPROVED")
                .build();
    }
}
