package com.grocery_store.payment.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.grocery_store.payment.model.ProductInfo;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
public class CartItem {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private ProductInfo productInfo;

    private int quantity;

    @JsonBackReference
    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    private Cart cart;
}
