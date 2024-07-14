package com.nothing.ecommerce.model;

import java.util.List;

import com.nothing.ecommerce.entity.Order;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class OrderViewModel {
    private int orderId;
    private String orderDate;
    private double totalAmount;
    // ('created','approved','processing','shipped','delivered','cancelled')
    private String status;
    private String shippingAddress;
    private List<ProductOrderResponse> products;

    public OrderViewModel(Order order, List<ProductOrderResponse> products) {
        this.orderId = order.getOrderId();
        this.orderDate = order.getOrderDate().toString();
        this.totalAmount = order.getTotalAmount();
        this.status = order.getStatus();
        this.shippingAddress = order.getShippingAddress();
        this.products = products;
    }
}
