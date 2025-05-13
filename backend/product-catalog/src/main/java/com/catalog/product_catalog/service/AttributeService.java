package com.catalog.product_catalog.service;

import com.catalog.product_catalog.dto.AttributeDTO;
import com.catalog.product_catalog.entity.Attribute;
import com.catalog.product_catalog.entity.Category;
import com.catalog.product_catalog.entity.Product;
import com.catalog.product_catalog.entity.ProductAttributeValue;
import com.catalog.product_catalog.repository.AttributeRepository;
import com.catalog.product_catalog.repository.CategoryRepository;
import com.catalog.product_catalog.repository.ProductAttributeValueRepository;
import com.catalog.product_catalog.repository.ProductRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class AttributeService {

    @Autowired
    private AttributeRepository attributeRepository;

    @Autowired
    private CategoryRepository categoryRepository;

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private ProductAttributeValueRepository productAttributeValueRepository;

    private AttributeDTO convertToDTO(Attribute attribute) {
        return new AttributeDTO(
                attribute.getId(),
                attribute.getName(),
                attribute.getDescription(),
                attribute.getCategory() != null ? attribute.getCategory().getId() : null,
                attribute.getDataType(),  // updated from getInputType()
                attribute.isMandatory()
        );
    }

    private Attribute convertToEntity(AttributeDTO dto) {
        Attribute attribute = new Attribute();
        attribute.setId(dto.getId());
        attribute.setName(dto.getName());
        attribute.setDescription(dto.getDescription());
        attribute.setDataType(dto.getDataType());  // updated from setInputType()
        attribute.setMandatory(dto.isMandatory());

        if (dto.getCategoryId() != null) {
            Category category = categoryRepository.findById(dto.getCategoryId()).orElse(null);
            attribute.setCategory(category);
        }

        return attribute;
    }

    @Transactional
    public AttributeDTO createAttribute(AttributeDTO dto) {
        Attribute attribute = convertToEntity(dto);
        Attribute savedAttribute = attributeRepository.save(attribute);

        List<Product> products = productRepository.findAll();
        for (Product product : products) {
            ProductAttributeValue productAttributeValue = new ProductAttributeValue();
            productAttributeValue.setProduct(product);
            productAttributeValue.setAttribute(savedAttribute);
            productAttributeValue.setValue("Default value");
//            productAttributeValue.setDataType(savedAttribute.getDataType());  // updated  karthik
            productAttributeValue.setType(savedAttribute.getDataType());
            productAttributeValueRepository.save(productAttributeValue);
        }

        return convertToDTO(savedAttribute);
    }

    public List<AttributeDTO> getAllAttributes() {
        return attributeRepository.findAll()
                .stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    public List<AttributeDTO> getAttributesByCategory(Long categoryId) {
        List<Attribute> attributes = attributeRepository.findByCategoryId(categoryId);
        return attributes.stream().map(this::convertToDTO).collect(Collectors.toList());
    }

    public AttributeDTO getAttributeById(Long id) {
        Attribute attribute = attributeRepository.findById(id).orElse(null);
        return attribute != null ? convertToDTO(attribute) : null;
    }

    public AttributeDTO updateAttribute(Long id, AttributeDTO dto) {
        if (attributeRepository.existsById(id)) {
            Attribute attribute = convertToEntity(dto);
            attribute.setId(id);
            Attribute updated = attributeRepository.save(attribute);
            return convertToDTO(updated);
        }
        return null;
    }

    @Transactional
    public boolean deleteAttribute(Long id) {
        if (!attributeRepository.existsById(id)) return false;

        productAttributeValueRepository.deleteByAttributeId(id);

        Attribute attribute = attributeRepository.findById(id).orElse(null);
        if (attribute != null && attribute.getCategory() != null) {
            Category category = attribute.getCategory();
            category.getAttributes().remove(attribute);
        }

        attributeRepository.deleteById(id);
        return true;
    }
}
