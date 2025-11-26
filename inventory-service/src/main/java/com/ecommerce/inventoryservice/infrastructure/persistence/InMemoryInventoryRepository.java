package com.ecommerce.inventoryservice.infrastructure.persistence;

import com.ecommerce.inventoryservice.domain.model.Inventory;
import com.ecommerce.inventoryservice.domain.repository.InventoryRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

/**
 * In-memory implementation of InventoryRepository.
 * Pre-populated with sample inventory data.
 */
@Repository
@Slf4j
public class InMemoryInventoryRepository implements InventoryRepository {

    private final Map<String, Inventory> inventoryStore = new ConcurrentHashMap<>();

    public InMemoryInventoryRepository() {
        // Pre-populate with sample inventory
        initializeSampleInventory();
    }

    private void initializeSampleInventory() {
        List<Inventory> sampleInventory = List.of(
                Inventory.builder()
                        .productId("PROD-001")
                        .productName("Laptop")
                        .availableQuantity(10)
                        .reservedQuantity(0)
                        .build(),
                Inventory.builder()
                        .productId("PROD-002")
                        .productName("Mouse")
                        .availableQuantity(50)
                        .reservedQuantity(0)
                        .build(),
                Inventory.builder()
                        .productId("PROD-003")
                        .productName("Keyboard")
                        .availableQuantity(30)
                        .reservedQuantity(0)
                        .build(),
                Inventory.builder()
                        .productId("PROD-004")
                        .productName("Monitor")
                        .availableQuantity(5)
                        .reservedQuantity(0)
                        .build(),
                Inventory.builder()
                        .productId("PROD-005")
                        .productName("Headphones")
                        .availableQuantity(0)
                        .reservedQuantity(0)
                        .build());

        sampleInventory.forEach(inventory -> {
            inventoryStore.put(inventory.getProductId(), inventory);
            log.info("Initialized inventory: {} - {} units",
                    inventory.getProductId(), inventory.getAvailableQuantity());
        });
    }

    @Override
    public Optional<Inventory> findByProductId(String productId) {
        return Optional.ofNullable(inventoryStore.get(productId));
    }

    @Override
    public List<Inventory> findAll() {
        return new ArrayList<>(inventoryStore.values());
    }

    @Override
    public Inventory save(Inventory inventory) {
        inventoryStore.put(inventory.getProductId(), inventory);
        return inventory;
    }

    @Override
    public void deleteByProductId(String productId) {
        inventoryStore.remove(productId);
    }
}
