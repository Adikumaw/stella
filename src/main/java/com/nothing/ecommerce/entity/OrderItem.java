package com.nothing.ecommerce.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
@Table(name = "order_items")
public class OrderItem {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "order_item_id")
    private int orderItemId;
    @Column(name = "order_id")
    private int orderId;
    @Column(name = "product_id")
    private int productId;
    // enum('waiting','accepted','canceled','shipped')
    @Column(name = "status")
    private String status;
    @Column(name = "quantity")
    private int quantity;
    @Column(name = "amount")
    private double price;
    @Column(name = "total_amount")
    private double totalPrice;

    public OrderItem(int orderId, int productId, int quantity, double price) {
        this.orderId = orderId;
        this.productId = productId;
        this.status = "waiting"; // set the initial status as waiting when creating an orderItem
        this.quantity = quantity;
        this.price = price;
        this.totalPrice = quantity * price;
    }

}
