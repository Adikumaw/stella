package com.nothing.ecommerce.model;

import com.nothing.ecommerce.entity.Product;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ProductOrderResponse {
    private int id;
    private String name;
    private String description;
    private double price;
    private int quantity;
    private String image;

    public ProductOrderResponse(Product product, int quantity) {
        this.id = product.getId();
        this.name = product.getName();
        this.description = product.getDescription();
        this.price = product.getPrice();
        this.quantity = quantity;
        this.image = product.getImage1();
    }
}
