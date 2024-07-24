package com.nothing.ecommerce.model;

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
    private String status;
    private double price;
    private double totalAmount;

    public SellerOrderViewModel(OrderItem orderItem, String name, StatusAndDateModel statusAndDate) {
        this.productId = orderItem.getProductId();
        this.name = name;
        this.quantity = orderItem.getQuantity();
        this.orderDate = statusAndDate.getDate().toString();
        this.status = statusAndDate.getStatus();
        this.price = orderItem.getPrice();
        this.totalAmount = orderItem.getTotalPrice();
    }
}
