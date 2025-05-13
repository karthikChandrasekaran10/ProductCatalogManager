package com.catalog.product_catalog.dto;

import lombok.Getter;
import lombok.Setter;


@Getter
@Setter
public class ProductAttributeValueDTO {
    // Getters and setters
    private Long id;
    private Long productId;  // ID of the product (not Product object)
    private Long attributeId;  // ID of the attribute
    private String value;

}
