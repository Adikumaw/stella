package com.nothing.stella.model;

import com.nothing.stella.entity.Order;

import lombok.Data;

@Data
public class OrderPaymentRequest {
    private int orderId;
    private String orderDate;
    private double totalAmount;
    // enum('creating','created','paid','processing','shipped','delivered','cancelled')
    private String status;
    private String shippingAddress;
    private String razorpayId;
    private String apiKey;

    public OrderPaymentRequest() {
    }

    public OrderPaymentRequest(int orderId, String orderDate, double totalAmount, String status, String shippingAddress,
            String razorpayId, String apiKey) {
        this.orderId = orderId;
        this.orderDate = orderDate;
        this.totalAmount = totalAmount;
        switch (status) {
            case "creating":
            case "created":
                this.status = "unpaid";
                break;
            default:
                this.status = status;
                break;
        }
        this.shippingAddress = shippingAddress;
        this.razorpayId = razorpayId;
        this.apiKey = apiKey;
    }

    public OrderPaymentRequest(Order order, String apiKey) {
        this.orderId = order.getOrderId();
        this.orderDate = order.getOrderDate().toString();
        this.totalAmount = order.getTotalAmount();
        switch (order.getStatus()) {
            case "creating":
            case "created":
                this.status = "unpaid";
                break;
            default:
                this.status = order.getStatus();
                break;
        }
        this.shippingAddress = order.getShippingAddress();
        this.razorpayId = order.getRazorpayId();
        this.apiKey = apiKey;
    }
}
