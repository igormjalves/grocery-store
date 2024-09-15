package com.grocery_store.payment.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@Entity
public class Cart {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private CartStatus status;

    @OneToMany(cascade = CascadeType.ALL)
    private List<CartItem> items;

    private Double grosslValue;
    private Double totalSavings;
    private Double netValue;
}
