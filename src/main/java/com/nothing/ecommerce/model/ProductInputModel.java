package com.nothing.ecommerce.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ProductInputModel {
    private String name;
    private String description;
    private double price;
    private int stock;
    private String category;
    private boolean active;
}
