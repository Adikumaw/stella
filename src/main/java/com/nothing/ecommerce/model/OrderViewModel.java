package com.nothing.ecommerce.model;

import java.util.List;

import com.nothing.ecommerce.entity.Order;

import lombok.Data;

@Data
public class OrderViewModel {
    private int orderId;
    private String orderDate;
    private double totalAmount;
    // enum('creating','created','paid','processing','shipped','delivered','cancelled')
    private String status;
    private String shippingAddress;
    private List<ProductOrderResponse> products;

    public OrderViewModel() {
    }

    public OrderViewModel(int orderId, String orderDate, double totalAmount, String status, String shippingAddress,
            List<ProductOrderResponse> products) {
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
        this.products = products;
    }

    public OrderViewModel(Order order, List<ProductOrderResponse> products) {
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
        this.products = products;
    }
}
