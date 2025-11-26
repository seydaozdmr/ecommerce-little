package com.ecommerce.inventoryservice.domain.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

/**
 * Inventory Domain Model.
 * Represents product inventory with available and reserved quantities.
 */
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class Inventory {

    private String productId;
    private String productName;
    private Integer availableQuantity;
    private Integer reservedQuantity;

    /**
     * Check if there is enough stock for the requested quantity.
     */
    public boolean hasStock(int requestedQuantity) {
        return availableQuantity != null && availableQuantity >= requestedQuantity;
    }

    /**
     * Reserve stock for an order.
     * Moves quantity from available to reserved.
     */
    public void reserve(int quantity) {
        if (!hasStock(quantity)) {
            throw new IllegalStateException(
                    String.format("Insufficient stock for product %s. Available: %d, Requested: %d",
                            productId, availableQuantity, quantity));
        }
        this.availableQuantity -= quantity;
        this.reservedQuantity = (reservedQuantity != null ? reservedQuantity : 0) + quantity;
    }

    /**
     * Release reserved stock back to available.
     */
    public void release(int quantity) {
        if (reservedQuantity == null || reservedQuantity < quantity) {
            throw new IllegalStateException(
                    String.format("Cannot release more than reserved. Reserved: %d, Requested: %d",
                            reservedQuantity, quantity));
        }
        this.reservedQuantity -= quantity;
        this.availableQuantity += quantity;
    }

    /**
     * Get current inventory status.
     */
    public InventoryStatus getStatus() {
        if (availableQuantity == null || availableQuantity == 0) {
            return InventoryStatus.OUT_OF_STOCK;
        }
        if (reservedQuantity != null && reservedQuantity > 0) {
            return InventoryStatus.RESERVED;
        }
        return InventoryStatus.AVAILABLE;
    }
}
