package com.ecommerce.orderservice.domain.event;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.time.LocalDateTime;

/**
 * OrderApproved Domain Event consumed from Kafka.
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
}
