package com.nothing.stella.model;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CartViewModel {
    private int cart_id;
    List<ProductOrderViewModel> products;
}
