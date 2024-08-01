package com.nothing.stella.entity;

import java.util.Date;

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
@Table(name = "orders")
public class Order {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "order_id")
    private int orderId;
    @Column(name = "user_id")
    private int userId;
    @Column(name = "order_date")
    private Date orderDate;
    @Column(name = "total_amount")
    private double totalAmount;
    // allowed values
    // enum('creating','created','paid','processing','shipped','delivered','cancelled')
    @Column(name = "status")
    private String status;
    @Column(name = "shipping_address")
    private String shippingAddress;
    @Column(name = "razorpay_id")
    private String razorpayId;
    @Column(name = "razorpay_payment_id")
    private String razorpayPaymentId;

    public Order(int userId, double totalAmount, String status, String shippingAddress, String razorpayId) {
        this.userId = userId;
        this.orderDate = new Date(); // Set current date and time
        this.totalAmount = totalAmount;
        this.status = status;
        this.shippingAddress = shippingAddress;
        this.razorpayId = razorpayId;
    }

}
