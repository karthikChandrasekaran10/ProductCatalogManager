package com.catalog.product_catalog.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.catalog.product_catalog.dto.ProductAttributeValueDTO;
import com.catalog.product_catalog.service.test;

import java.util.List;

@RestController
@RequestMapping("/api/product-attribute-values")
@RequiredArgsConstructor
public class ProductAttributeValueController {

    private final test productAttributeValueService;

    // Create new ProductAttributeValue
    @PostMapping
    public ResponseEntity<ProductAttributeValueDTO> create(@RequestBody ProductAttributeValueDTO pavDto) {
        ProductAttributeValueDTO savedDto = productAttributeValueService.save(pavDto);
        return ResponseEntity.ok(savedDto);
    }

    // Get all ProductAttributeValues for a product
    @GetMapping("/product/{productId}")
    public ResponseEntity<List<ProductAttributeValueDTO>> getByProduct(@PathVariable Long productId) {
        List<ProductAttributeValueDTO> values = productAttributeValueService.getAllValuesForProduct(productId);
        return ResponseEntity.ok(values);
    }

    // Update an existing ProductAttributeValue
    @PutMapping("/{id}")
    public ResponseEntity<ProductAttributeValueDTO> update(
            @PathVariable Long id,
            @RequestBody ProductAttributeValueDTO updatedValue) {
        ProductAttributeValueDTO updatedDto = productAttributeValueService.update(id, updatedValue);
        return ResponseEntity.ok(updatedDto);
    }

    // Delete an existing ProductAttributeValue
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        productAttributeValueService.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}
