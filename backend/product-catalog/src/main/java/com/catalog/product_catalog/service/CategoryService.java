package com.catalog.product_catalog.service;

import com.catalog.product_catalog.dto.CategoryDTO;
import com.catalog.product_catalog.dto.AttributeDTO;
import com.catalog.product_catalog.entity.Category;
import com.catalog.product_catalog.entity.Attribute;
import com.catalog.product_catalog.repository.CategoryRepository;
import com.catalog.product_catalog.repository.AttributeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class CategoryService {

    @Autowired
    private CategoryRepository categoryRepository;

    @Autowired
    private AttributeRepository attributeRepository;

    // Convert Category entity to CategoryDTO
    private CategoryDTO convertToDTO(Category category) {
        List<AttributeDTO> attributeDTOs = category.getAttributes() != null
                ? category.getAttributes().stream()
                .map(attribute -> new AttributeDTO(
                        attribute.getId(),
                        attribute.getName(),
                        attribute.getDescription(),
                        category.getId(),
                        attribute.getDataType(),
                        attribute.isMandatory()
                ))
                .collect(Collectors.toList())
                : new ArrayList<>();

        return new CategoryDTO(
                category.getId(),
                category.getName(),
                category.getDescription(),
                attributeDTOs
        );
    }

    // Convert CategoryDTO to Category entity
    private Category convertToEntity(CategoryDTO categoryDTO) {
        Category category = new Category();
        category.setName(categoryDTO.getName());
        category.setDescription(categoryDTO.getDescription());
        return category;
    }

    // Create a new category with attributes
    public CategoryDTO createCategory(CategoryDTO categoryDTO) {
        Category category = convertToEntity(categoryDTO);
        category = categoryRepository.save(category); // Save to get generated ID

        if (categoryDTO.getAttributes() != null) {
            for (AttributeDTO attributeDTO : categoryDTO.getAttributes()) {
                Attribute attribute = new Attribute();
                attribute.setName(attributeDTO.getName());
                attribute.setDescription(attributeDTO.getDescription());
                attribute.setCategory(category);
                attribute.setDataType(attributeDTO.getDataType());
                attribute.setMandatory(attributeDTO.isMandatory());
                attributeRepository.save(attribute);
            }
        }

        return convertToDTO(categoryRepository.findById(category.getId()).orElse(category));
    }

    // Get all categories
    public List<CategoryDTO> getAllCategories() {
        List<Category> categories = categoryRepository.findAll();
        return categories.stream().map(this::convertToDTO).collect(Collectors.toList());
    }

    // Get a category by ID
    public CategoryDTO getCategoryById(Long id) {
        Category category = categoryRepository.findById(id).orElse(null);
        return category != null ? convertToDTO(category) : null;
    }

    // Update a category and manage its attributes
    public CategoryDTO updateCategory(Long id, CategoryDTO categoryDTO) {
        if (!categoryRepository.existsById(id)) {
            return null;
        }

        Category category = convertToEntity(categoryDTO);
        category.setId(id);
        Category updatedCategory = categoryRepository.save(category);

        List<AttributeDTO> incomingAttributes = categoryDTO.getAttributes() != null
                ? categoryDTO.getAttributes()
                : new ArrayList<>();

        List<Attribute> existingAttributes = attributeRepository.findByCategoryId(id);

        // Delete attributes that are no longer present
        for (Attribute existingAttribute : existingAttributes) {
            boolean stillExists = incomingAttributes.stream()
                    .anyMatch(dto -> dto.getId() != null && dto.getId().equals(existingAttribute.getId()));
            if (!stillExists) {
                attributeRepository.delete(existingAttribute);
            }
        }

        // Add or update attributes
        for (AttributeDTO attributeDTO : incomingAttributes) {
            Attribute attribute;
            if (attributeDTO.getId() != null) {
                attribute = attributeRepository.findById(attributeDTO.getId()).orElse(new Attribute());
            } else {
                attribute = new Attribute();
            }

            attribute.setName(attributeDTO.getName());
            attribute.setDescription(attributeDTO.getDescription());
            attribute.setDataType(attributeDTO.getDataType());
            attribute.setMandatory(attributeDTO.isMandatory());
            attribute.setCategory(updatedCategory);
            attributeRepository.save(attribute);
        }

        return convertToDTO(updatedCategory);
    }

    // Delete a category and its attributes
    public boolean deleteCategory(Long id) {
        if (!categoryRepository.existsById(id)) {
            return false;
        }

        List<Attribute> attributes = attributeRepository.findByCategoryId(id);
        attributeRepository.deleteAll(attributes);
        categoryRepository.deleteById(id);
        return true;
    }
}
