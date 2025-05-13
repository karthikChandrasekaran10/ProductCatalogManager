package com.catalog.product_catalog.service;

import com.catalog.product_catalog.dto.ProductAttributeValueDTO;
import com.catalog.product_catalog.entity.ProductAttributeValue;
import com.catalog.product_catalog.entity.Product;
import com.catalog.product_catalog.repository.ProductAttributeValueRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class test {

    private final ProductAttributeValueRepository repository;

    // Helper method to convert ProductAttributeValueDto to ProductAttributeValue
    private ProductAttributeValue mapToEntity(ProductAttributeValueDTO dto) {
        ProductAttributeValue entity = new ProductAttributeValue();

        // Create a Product entity and set the product ID
        Product product = new Product();
        product.setId(dto.getProductId());  // Set the product ID on the Product entity

        // Set the Product and other attributes
        entity.setProduct(product);
        entity.setId(dto.getId());
//        entity.setAttributeId(dto.getAttributeId());  karthik
        entity.setId(dto.getAttributeId());
        entity.setValue(dto.getValue());

        return entity;
    }

    // Helper method to convert ProductAttributeValue to ProductAttributeValueDto
    private ProductAttributeValueDTO mapToDto(ProductAttributeValue entity) {
        ProductAttributeValueDTO dto = new ProductAttributeValueDTO();
        dto.setId(entity.getId());
        dto.setProductId(entity.getProduct().getId());  // Get the product ID
        dto.setAttributeId(entity.getAttribute().getId());  // Get the attribute ID
        dto.setValue(entity.getValue());
        return dto;
    }

    // Get all attribute values for a specific product
    public List<ProductAttributeValueDTO> getAllValuesForProduct(Long productId) {
        List<ProductAttributeValue> entities = repository.findByProductId(productId);
        return entities.stream()
                .map(this::mapToDto)
                .collect(Collectors.toList());
    }

    // Save a single ProductAttributeValue
    public ProductAttributeValueDTO save(ProductAttributeValueDTO dto) {
        ProductAttributeValue entity = mapToEntity(dto);
        ProductAttributeValue savedEntity = repository.save(entity);
        return mapToDto(savedEntity);
    }

    // Get a specific ProductAttributeValue by its ID
    public Optional<ProductAttributeValueDTO> getById(Long id) {
        Optional<ProductAttributeValue> entity = repository.findById(id);
        return entity.map(this::mapToDto);
    }

    // Delete a ProductAttributeValue by its ID
    public void deleteById(Long id) {
        repository.deleteById(id);
    }

    // Save multiple ProductAttributeValue entities
    public List<ProductAttributeValueDTO> saveAll(List<ProductAttributeValueDTO> dtos) {
        List<ProductAttributeValue> entities = dtos.stream()
                .map(this::mapToEntity)
                .collect(Collectors.toList());
        List<ProductAttributeValue> savedEntities = repository.saveAll(entities);
        return savedEntities.stream()
                .map(this::mapToDto)
                .collect(Collectors.toList());
    }

    // Update an existing ProductAttributeValue (only the value can be updated)
    public ProductAttributeValueDTO update(Long id, ProductAttributeValueDTO updatedDto) {
        ProductAttributeValue existing = repository.findById(id)
                .orElseThrow(() -> new RuntimeException("Attribute value not found"));

        existing.setValue(updatedDto.getValue());  // Only update the value
        ProductAttributeValue savedEntity = repository.save(existing);
        return mapToDto(savedEntity);
    }
}
