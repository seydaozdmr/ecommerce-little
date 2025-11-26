package com.ecommerce.inventoryservice.application.service;

import com.ecommerce.inventoryservice.application.dto.CreateInventoryRequest;
import com.ecommerce.inventoryservice.application.dto.InventoryResponse;
import com.ecommerce.inventoryservice.domain.event.OrderCreatedEvent;

import java.util.List;

/**
 * Service interface for inventory operations.
 */
public interface InventoryService {

    /**
     * Process an order and check inventory availability.
     * Returns true if all items are available and reserved.
     */
    boolean processOrder(OrderCreatedEvent orderEvent);

    /**
     * Get inventory by product ID.
     */
    InventoryResponse getInventory(String productId);

    /**
     * Get all inventory items.
     */
    List<InventoryResponse> getAllInventory();

    /**
     * Create or update inventory.
     */
    InventoryResponse createOrUpdateInventory(CreateInventoryRequest request);
}
