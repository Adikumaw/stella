package com.nothing.stella.model;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CartUpdateRequest {
    private int cartId;
    private List<ProductOrderRequest> updates;
}
