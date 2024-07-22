package com.nothing.ecommerce.model;

import java.util.Date;

import com.nothing.ecommerce.entity.OrderItem;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class SellerOrderViewModel {
    private int productId;
    private String name;
    private int quantity;
    private String orderDate;
    private double price;
    private double totalAmount;

    public SellerOrderViewModel(OrderItem orderItem, String name, Date orderDate) {
        this.productId = orderItem.getProductId();
        this.name = name;
        this.quantity = orderItem.getQuantity();
        this.orderDate = orderDate.toString();
        this.price = orderItem.getPrice();
        this.totalAmount = orderItem.getTotalPrice();
    }
}
