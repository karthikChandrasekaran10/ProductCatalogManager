package com.catalog.product_catalog.repository;

import com.catalog.product_catalog.entity.Category;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CategoryRepository extends JpaRepository<Category, Long> {
    // JpaRepository provides basic CRUD methods, you can define custom queries here if needed
}
