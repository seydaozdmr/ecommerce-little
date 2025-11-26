package com.ecommerce.inventoryservice.application.service;

import com.ecommerce.inventoryservice.application.dto.CreateInventoryRequest;
import com.ecommerce.inventoryservice.application.dto.InventoryResponse;
import com.ecommerce.inventoryservice.domain.event.OrderCreatedEvent;
import com.ecommerce.inventoryservice.domain.exception.InventoryException;
import com.ecommerce.inventoryservice.domain.model.Inventory;
import com.ecommerce.inventoryservice.domain.repository.InventoryRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Implementation of InventoryService.
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class InventoryServiceImpl implements InventoryService {

    private final InventoryRepository inventoryRepository;

    @Override
    public boolean processOrder(OrderCreatedEvent orderEvent) {
        log.info("Processing order: {}", orderEvent.getOrderId());

        try {
            // Check all items first
            for (OrderCreatedEvent.OrderItemEvent item : orderEvent.getItems()) {
                Inventory inventory = inventoryRepository.findByProductId(item.getProductId())
                        .orElseThrow(() -> new InventoryException(
                                "Product not found: " + item.getProductId()));

                if (!inventory.hasStock(item.getQuantity())) {
                    log.warn("Insufficient stock for product {}. Available: {}, Requested: {}",
                            item.getProductId(), inventory.getAvailableQuantity(), item.getQuantity());
                    return false;
                }
            }

            // If all items are available, reserve them
            for (OrderCreatedEvent.OrderItemEvent item : orderEvent.getItems()) {
                Inventory inventory = inventoryRepository.findByProductId(item.getProductId())
                        .orElseThrow(() -> new InventoryException(
                                "Product not found: " + item.getProductId()));

                inventory.reserve(item.getQuantity());
                inventoryRepository.save(inventory);
                log.info("Reserved {} units of product {}", item.getQuantity(), item.getProductId());
            }

            log.info("Order {} processed successfully", orderEvent.getOrderId());
            return true;

        } catch (Exception e) {
            log.error("Error processing order {}: {}", orderEvent.getOrderId(), e.getMessage());
            return false;
        }
    }

    @Override
    public InventoryResponse getInventory(String productId) {
        Inventory inventory = inventoryRepository.findByProductId(productId)
                .orElseThrow(() -> new InventoryException("Product not found: " + productId));

        return mapToResponse(inventory);
    }

    @Override
    public List<InventoryResponse> getAllInventory() {
        return inventoryRepository.findAll().stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    @Override
    public InventoryResponse createOrUpdateInventory(CreateInventoryRequest request) {
        Inventory inventory = Inventory.builder()
                .productId(request.getProductId())
                .productName(request.getProductName())
                .availableQuantity(request.getAvailableQuantity())
                .reservedQuantity(request.getReservedQuantity() != null ? request.getReservedQuantity() : 0)
                .build();

        Inventory saved = inventoryRepository.save(inventory);
        log.info("Inventory created/updated for product: {}", saved.getProductId());

        return mapToResponse(saved);
    }

    private InventoryResponse mapToResponse(Inventory inventory) {
        return InventoryResponse.builder()
                .productId(inventory.getProductId())
                .productName(inventory.getProductName())
                .availableQuantity(inventory.getAvailableQuantity())
                .reservedQuantity(inventory.getReservedQuantity())
                .status(inventory.getStatus())
                .build();
    }
}
