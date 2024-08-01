package com.nothing.stella.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AddToCartModel {
    private int cartId;
    private ProductOrderRequest request;
}
