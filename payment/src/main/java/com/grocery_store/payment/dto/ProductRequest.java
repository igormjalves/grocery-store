package com.grocery_store.payment.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ProductRequest {

    @NotBlank(message = "Product ID is mandatory")
    private String productId;

    @Min(value = 1, message = "Quantity must be at least 1")
    @NotNull(message = "Quantity is mandatory")
    private Integer quantity;

}
