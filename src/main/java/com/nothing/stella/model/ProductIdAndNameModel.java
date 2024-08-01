package com.nothing.stella.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ProductIdAndNameModel {
    private int productId;
    private String name;
}
