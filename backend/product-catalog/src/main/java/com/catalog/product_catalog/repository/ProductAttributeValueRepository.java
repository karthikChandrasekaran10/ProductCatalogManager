package com.catalog.product_catalog.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.catalog.product_catalog.entity.ProductAttributeValue;

import java.util.List;

public interface ProductAttributeValueRepository extends JpaRepository<ProductAttributeValue, Long> {
    List<ProductAttributeValue> findByProductId(Long productId);
    void deleteByAttributeId(Long id); // karthik   // added this as a dummy
}
