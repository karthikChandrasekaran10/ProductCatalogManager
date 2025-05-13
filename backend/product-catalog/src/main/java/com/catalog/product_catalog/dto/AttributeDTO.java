package com.catalog.product_catalog.dto;

import com.catalog.product_catalog.entity.AttributeDataType;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
//import jakarta.validation.constraints.NotNull;
//import jakarta.validation.constraints.Size;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class AttributeDTO {

    private Long id;

//    @NotNull(message = "Name cannot be null")
//    @Size(min = 2, max = 100, message = "Name must be between 2 and 100 characters")
    private String name;

//    @Size(max = 255, message = "Description cannot exceed 255 characters")
    private String description;

//    @NotNull(message = "Category ID cannot be null")
    private Long categoryId;

//    @NotNull(message = "Data type cannot be null")
    private AttributeDataType dataType; // renamed from inputType

    private boolean mandatory;
}
