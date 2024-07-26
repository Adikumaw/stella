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
    private int id; // order Item ID
    private int productId;
    private String name;
    private int quantity;
    private String orderDate;
    private String status;
    private double price;
    private double totalAmount;

    public SellerOrderViewModel(OrderItem orderItem, String name, Date orderDate) {
        this.id = orderItem.getOrderItemId();
        this.productId = orderItem.getProductId();
        this.name = name;
        this.quantity = orderItem.getQuantity();
        this.orderDate = orderDate.toString();
        this.status = orderItem.getStatus();
        this.price = orderItem.getPrice();
        this.totalAmount = orderItem.getTotalPrice();
    }
}
