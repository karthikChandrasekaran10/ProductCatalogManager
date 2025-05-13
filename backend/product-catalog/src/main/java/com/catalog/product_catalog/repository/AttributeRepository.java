package com.catalog.product_catalog.repository;

import com.catalog.product_catalog.entity.Attribute;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AttributeRepository extends JpaRepository<Attribute, Long> {
    // Custom queries for attributes can be defined here
    List<Attribute> findByCategoryId(Long categoryId);
}
