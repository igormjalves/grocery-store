package com.grocery_store.payment.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class Promotion {

    private String type;
    private int required_qty;
    private int price;
    private int free_qty;
    private int amount;

}
