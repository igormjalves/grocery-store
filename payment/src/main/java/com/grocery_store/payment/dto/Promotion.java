package com.grocery_store.payment.dto;

public record Promotion(
        String type,
        int required_qty,
        int price,
        int free_qty,
        int amount
) { }
