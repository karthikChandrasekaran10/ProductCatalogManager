package com.catalog.product_catalog.controller;

import com.catalog.product_catalog.dto.ProductAttributeValueDTO;
import com.catalog.product_catalog.dto.ProductWithAttributesDTO;
import com.catalog.product_catalog.dto.ProductDTO;
import com.catalog.product_catalog.service.ProductAttributeValueService;
import com.catalog.product_catalog.service.ProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@CrossOrigin(origins = "*")
@RestController
@RequestMapping("/api/products")

public class ProductController {

    @Autowired
    private ProductService productService;
    @Autowired
    private ProductAttributeValueService productAttributeValueService;
    // Create a new product
//    @PostMapping
//    public ResponseEntity<ProductDTO> createProduct(@RequestBody ProductDTO productDTO) {
//        ProductDTO createdProduct = productService.createProduct(productDTO);
//        return new ResponseEntity<>(createdProduct, HttpStatus.CREATED);
//    }
    @PostMapping
    public ResponseEntity<?> createProduct(@RequestBody ProductWithAttributesDTO productDTO) {
        try {
            ProductDTO baseProduct = new ProductDTO(
                    null,
                    productDTO.getName(),
                    productDTO.getDescription(),
                    productDTO.getCategoryId()
            );

            ProductDTO savedProduct = productService.createProduct(baseProduct);

            if (savedProduct == null) {
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Product could not be saved.");
            }

            Long productId = savedProduct.getId();

            if (productDTO.getAttributeValues() != null) {
                for (ProductAttributeValueDTO pavDTO : productDTO.getAttributeValues()) {
                    pavDTO.setProductId(productId);
                    productAttributeValueService.save(pavDTO);
                }
            }

            return ResponseEntity.status(HttpStatus.CREATED).body(savedProduct);

        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error creating product: " + e.getMessage());
        }
    }



    // Get all products
    @GetMapping
    public ResponseEntity<List<ProductDTO>> getAllProducts() {
        List<ProductDTO> products = productService.getAllProducts();
        return new ResponseEntity<>(products, HttpStatus.OK);
    }

    // Get a product by id
    @GetMapping("/{id}")
    public ResponseEntity<ProductWithAttributesDTO> getProductById(@PathVariable Long id) {
        ProductWithAttributesDTO dto = productService.getProductWithAttributesById(id);
        return ResponseEntity.ok(dto);
    }


    // Update a product
    @PutMapping("/{id}")
    public ResponseEntity<?> updateProduct(@PathVariable Long id, @RequestBody ProductWithAttributesDTO productDTO) {
        try {
            ProductDTO baseProduct = new ProductDTO(
                    id,
                    productDTO.getName(),
                    productDTO.getDescription(),
                    productDTO.getCategoryId()
            );

            ProductDTO updatedProduct = productService.updateProduct(id, baseProduct);
            if (updatedProduct == null) {
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
            }

            // First delete old attribute values
            productAttributeValueService.deleteByProductId(id);

            // Then save new ones
            if (productDTO.getAttributeValues() != null) {
                for (ProductAttributeValueDTO pavDTO : productDTO.getAttributeValues()) {
                    pavDTO.setProductId(id);
                    productAttributeValueService.save(pavDTO);
                }
            }

            return new ResponseEntity<>(updatedProduct, HttpStatus.OK);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error updating product: " + e.getMessage());
        }
    }


    // Delete a product
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteProduct(@PathVariable Long id) {
        if (productService.deleteProduct(id)) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }
}

