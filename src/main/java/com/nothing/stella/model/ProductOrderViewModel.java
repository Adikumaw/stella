package com.nothing.stella.model;

import com.nothing.stella.entity.Product;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ProductOrderViewModel {
    private int id;
    private String name;
    private double price;
    private int quantity;
    private String image;

    public ProductOrderViewModel(Product product, int quantity) {
        this.id = product.getId();
        this.name = product.getName();
        this.price = product.getPrice();
        this.quantity = quantity;
        this.image = product.getImage1();
    }
}
