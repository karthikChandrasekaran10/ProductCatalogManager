package com.catalog.product_catalog.service;

import com.catalog.product_catalog.dto.ProductAttributeValueDTO;
import com.catalog.product_catalog.dto.ProductDTO;
import com.catalog.product_catalog.dto.ProductWithAttributesDTO;
import com.catalog.product_catalog.entity.Product;
import com.catalog.product_catalog.entity.Category;
import com.catalog.product_catalog.repository.ProductRepository;
import com.catalog.product_catalog.repository.CategoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;


@Service
public class ProductService {

    private final ProductRepository productRepository;
    private final ProductAttributeValueService pavService;
    private final CategoryRepository categoryRepository;

    public ProductService(ProductRepository productRepository,
                          ProductAttributeValueService pavService,
                          CategoryRepository categoryRepository) {
        this.productRepository = productRepository;
        this.pavService = pavService;
        this.categoryRepository = categoryRepository;
    }



    // Convert Product entity to ProductDTO
    private ProductDTO convertToDTO(Product product) {
        return new ProductDTO(
                product.getId(),
                product.getName(),
                product.getDescription(),
                product.getCategory().getId()
        );
    }

    // Convert ProductDTO to Product entity
    private Product convertToEntity(ProductDTO productDTO) {
        Product product = new Product();
        product.setId(productDTO.getId());
        product.setName(productDTO.getName());
        product.setDescription(productDTO.getDescription());

        // Get the category based on categoryId
        Category category = getCategoryById(productDTO.getCategoryId());
        product.setCategory(category);

        return product;
    }

    // Get a category by its ID (could be handled via a service or repository)
    private Category getCategoryById(Long categoryId) {
        return categoryRepository.findById(categoryId)
                .orElseThrow(() -> new RuntimeException("Category not found"));
    }

    // Create a new product
    public ProductDTO createProduct(ProductDTO productDTO) {
        Product product = convertToEntity(productDTO);
        Product savedProduct = productRepository.save(product);
        return convertToDTO(savedProduct);
    }

    // Get all products
    public List<ProductDTO> getAllProducts() {
        List<Product> products = productRepository.findAll();
        return products.stream().map(this::convertToDTO).collect(Collectors.toList());
    }

    // Get a product by ID
    public ProductDTO getProductById(Long id) {
        Optional<Product> product = productRepository.findById(id);
        return product.map(this::convertToDTO).orElse(null);
    }

    // Update a product
    public ProductDTO updateProduct(Long id, ProductDTO productDTO) {
        Optional<Product> productOpt = productRepository.findById(id);
        if (productOpt.isPresent()) {
            Product product = convertToEntity(productDTO);
            product.setId(id);
            Product updatedProduct = productRepository.save(product);
            return convertToDTO(updatedProduct);
        }
        return null;
    }

    // Delete a product
    public boolean deleteProduct(Long id) {
        if (productRepository.existsById(id)) {
            productRepository.deleteById(id);
            return true;
        }
        return false;
    }
    public ProductWithAttributesDTO getProductWithAttributesById(Long id) {
        Product product = productRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Product not found"));

        ProductWithAttributesDTO dto = new ProductWithAttributesDTO();
        dto.setId(product.getId());
        dto.setName(product.getName());
        dto.setDescription(product.getDescription());
        dto.setCategoryId(product.getCategory().getId());

        List<ProductAttributeValueDTO> attrDTOs = product.getAttributeValues().stream().map(attrVal -> {
            ProductAttributeValueDTO attrDTO = new ProductAttributeValueDTO();
            attrDTO.setId(attrVal.getId());
            attrDTO.setAttributeId(attrVal.getAttribute().getId());
            attrDTO.setAttributeName(attrVal.getAttribute().getName());
            attrDTO.setDatatype(attrVal.getAttribute().getDataType().name()); // e.g. STRING, NUMBER
            attrDTO.setValue(attrVal.getValue());
            return attrDTO;
        }).collect(Collectors.toList());

        dto.setAttributeValues(attrDTOs);
        return dto;
    }



}
