package com.ecommerce.inventoryservice.application.dto;

import com.ecommerce.inventoryservice.domain.model.InventoryStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * DTO for inventory responses.
 */
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class InventoryResponse {

    private String productId;
    private String productName;
    private Integer availableQuantity;
    private Integer reservedQuantity;
    private InventoryStatus status;
}
