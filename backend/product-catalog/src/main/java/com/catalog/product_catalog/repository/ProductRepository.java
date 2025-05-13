package com.catalog.product_catalog.repository;

import com.catalog.product_catalog.entity.Product;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProductRepository extends JpaRepository<Product, Long> {
}
