package com.nothing.stella.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CartItemViewModel {
    private int product_id;
    private int quantity;
    private Double price;
    private Double totalPrice;
}
