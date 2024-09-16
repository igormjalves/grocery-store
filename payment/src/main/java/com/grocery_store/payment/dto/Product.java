package com.grocery_store.payment.dto;

import java.util.List;

public record Product (
    String id,
    String name,
    double price,
    List<Promotion> promotions
) {}
