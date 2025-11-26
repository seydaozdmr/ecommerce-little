package com.ecommerce.inventoryservice.domain.repository;

import com.ecommerce.inventoryservice.domain.model.Inventory;

import java.util.List;
import java.util.Optional;

/**
 * Repository interface for Inventory domain model.
 */
public interface InventoryRepository {

    Optional<Inventory> findByProductId(String productId);

    List<Inventory> findAll();

    Inventory save(Inventory inventory);

    void deleteByProductId(String productId);
}
