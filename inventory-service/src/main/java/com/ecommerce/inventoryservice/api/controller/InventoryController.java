package com.ecommerce.inventoryservice.api.controller;

import com.ecommerce.inventoryservice.application.dto.CreateInventoryRequest;
import com.ecommerce.inventoryservice.application.dto.InventoryResponse;
import com.ecommerce.inventoryservice.application.service.InventoryService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * REST controller for inventory management.
 */
@RestController
@RequestMapping("/api/inventory")
@RequiredArgsConstructor
@Slf4j
@Tag(name = "Inventory", description = "Inventory management API")
public class InventoryController {

    private final InventoryService inventoryService;

    @GetMapping
    @Operation(summary = "Get all inventory items")
    public ResponseEntity<List<InventoryResponse>> getAllInventory() {
        log.info("GET /api/inventory - Get all inventory");
        List<InventoryResponse> inventory = inventoryService.getAllInventory();
        return ResponseEntity.ok(inventory);
    }

    @GetMapping("/{productId}")
    @Operation(summary = "Get inventory by product ID")
    public ResponseEntity<InventoryResponse> getInventory(@PathVariable String productId) {
        log.info("GET /api/inventory/{} - Get inventory by product ID", productId);
        InventoryResponse inventory = inventoryService.getInventory(productId);
        return ResponseEntity.ok(inventory);
    }

    @PostMapping
    @Operation(summary = "Create or update inventory")
    public ResponseEntity<InventoryResponse> createOrUpdateInventory(
            @Valid @RequestBody CreateInventoryRequest request) {
        log.info("POST /api/inventory - Create/update inventory: {}", request);
        InventoryResponse inventory = inventoryService.createOrUpdateInventory(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(inventory);
    }
}
