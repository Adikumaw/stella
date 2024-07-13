package com.nothing.ecommerce.model;

import com.nothing.ecommerce.entity.Order;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class OrderResponse {
    private int orderId;
    private String orderDate;
    private double totalAmount;
    // ('created','approved','processing','shipped','delivered','cancelled')
    private String status;
    private String shippingAddress;
    private String razorpayId;

    public OrderResponse(Order order) {
        this.orderId = order.getOrderId();
        this.orderDate = order.getOrderDate().toString();
        this.totalAmount = order.getTotalAmount();
        this.status = order.getStatus();
        this.shippingAddress = order.getShippingAddress();
        this.razorpayId = order.getRazorpayId();
    }
}
