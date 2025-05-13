package com.catalog.product_catalog.controller;

import com.catalog.product_catalog.dto.AttributeDTO;
import com.catalog.product_catalog.service.AttributeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


import java.util.List;

@CrossOrigin(origins = "*")
@RestController
@RequestMapping("/api/attributes")
public class AttributeController {
    private static final Logger LOGGER = LoggerFactory.getLogger(AttributeController.class);
    @Autowired
    private AttributeService attributeService;

    // Create a new attribute
    @PostMapping
    public ResponseEntity<AttributeDTO> createAttribute(@RequestBody AttributeDTO attributeDTO) {
        AttributeDTO createdAttribute = attributeService.createAttribute(attributeDTO);
        return new ResponseEntity<>(createdAttribute, HttpStatus.CREATED);
    }

    // Get all attributes
    @GetMapping
    public ResponseEntity<List<AttributeDTO>> getAllAttributes() {
        List<AttributeDTO> attributes = attributeService.getAllAttributes();
        return new ResponseEntity<>(attributes, HttpStatus.OK);
    }

    // Get attributes by category ID
    @GetMapping("/category/{categoryId}")
    public ResponseEntity<List<AttributeDTO>> getAttributesByCategory(@PathVariable Long categoryId) {
        List<AttributeDTO> attributes = attributeService.getAttributesByCategory(categoryId);
        return new ResponseEntity<>(attributes, HttpStatus.OK);
    }

    // Get a single attribute by ID
    @GetMapping("/{id}")
    public ResponseEntity<AttributeDTO> getAttributeById(@PathVariable Long id) {
        AttributeDTO attribute = attributeService.getAttributeById(id);
        if (attribute != null) {
            return new ResponseEntity<>(attribute, HttpStatus.OK);
        }
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    // Update an attribute
    @PutMapping("/{id}")
    public ResponseEntity<AttributeDTO> updateAttribute(@PathVariable Long id, @RequestBody AttributeDTO attributeDTO) {
        AttributeDTO updatedAttribute = attributeService.updateAttribute(id, attributeDTO);
        if (updatedAttribute != null) {
            return new ResponseEntity<>(updatedAttribute, HttpStatus.OK);
        }
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    // Delete an attribute
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteAttribute(@PathVariable Long id) {
        LOGGER.info("Deleting attribute with id: " + id);
        if (attributeService.deleteAttribute(id)) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }
}
