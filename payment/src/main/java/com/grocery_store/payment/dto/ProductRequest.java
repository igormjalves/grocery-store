package com.grocery_store.payment.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ProductRequest {

    @NotNull(message = "Product ID is mandatory")
    private String productId;

    @Positive(message = "Quantity must be at least 1")
    @NotNull(message = "Quantity is mandatory")
    private Integer quantity;

}
