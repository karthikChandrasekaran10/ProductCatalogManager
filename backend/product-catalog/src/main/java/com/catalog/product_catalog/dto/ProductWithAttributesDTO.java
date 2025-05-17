package com.catalog.product_catalog.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ProductWithAttributesDTO {

    private Long id;
    private String name;
    private String description;
    private Long categoryId;
    private List<ProductAttributeValueDTO> attributeValues;
}
