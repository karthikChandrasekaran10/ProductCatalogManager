package com.catalog.product_catalog.service;

import com.catalog.product_catalog.dto.ProductAttributeValueDTO;
import com.catalog.product_catalog.entity.Attribute;
import com.catalog.product_catalog.entity.Product;
import com.catalog.product_catalog.entity.ProductAttributeValue;
import com.catalog.product_catalog.repository.AttributeRepository;
import com.catalog.product_catalog.repository.ProductAttributeValueRepository;
import com.catalog.product_catalog.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ProductAttributeValueService {

    private final ProductAttributeValueRepository pavRepository;
    private final ProductRepository productRepository;
    private final AttributeRepository attributeRepository;

    public ProductAttributeValueDTO save(ProductAttributeValueDTO dto) {
        Product product = productRepository.findById(dto.getProductId())
                .orElseThrow(() -> new RuntimeException("Product not found"));

        Attribute attribute = attributeRepository.findById(dto.getAttributeId())
                .orElseThrow(() -> new RuntimeException("Attribute not found"));

        ProductAttributeValue pav = ProductAttributeValue.builder()
                .product(product)
                .attribute(attribute)
                .value(dto.getValue())
                .type(attribute.getDataType())
                .build();

        return mapToDTO(pavRepository.save(pav));
    }

    public List<ProductAttributeValueDTO> getAllValuesForProduct(Long productId) {
        return pavRepository.findByProductId(productId).stream()
                .map(this::mapToDTO)
                .collect(Collectors.toList());
    }

    public ProductAttributeValueDTO update(Long id, ProductAttributeValueDTO dto) {
        ProductAttributeValue pav = pavRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("ProductAttributeValue not found"));

        if (dto.getValue() != null) {
            pav.setValue(dto.getValue());
        }

        return mapToDTO(pavRepository.save(pav));
    }

    public void deleteByProductId(Long productId) {
        List<ProductAttributeValue> values = pavRepository.findByProductId(productId);
        pavRepository.deleteAll(values);
    }


    private ProductAttributeValueDTO mapToDTO(ProductAttributeValue pav) {
        ProductAttributeValueDTO dto = new ProductAttributeValueDTO();
        dto.setId(pav.getId());
        dto.setProductId(pav.getProduct().getId());
        dto.setAttributeId(pav.getAttribute().getId());
        dto.setValue(pav.getValue());
        return dto;
    }
}
