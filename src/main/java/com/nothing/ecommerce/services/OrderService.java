package com.nothing.ecommerce.services;

import com.nothing.ecommerce.model.OrderRequest;
import com.nothing.ecommerce.model.OrderViewModel;
import com.nothing.ecommerce.model.PaymentCallbackRequest;

import java.util.List;

import com.nothing.ecommerce.entity.Order;
import com.nothing.ecommerce.model.OrderPaymentRequest;

public interface OrderService {
    OrderPaymentRequest create(String reference, OrderRequest orderRequest);

    void handlePaymentCallback(PaymentCallbackRequest request);

    List<OrderViewModel> fetchOrders(String reference);

    String fetchOrderStatus(int orderId);

    Order checkRazorpayStatus(Order order);

    void emailSender(int userId, OrderViewModel orderViewModel);

    OrderViewModel fetchOrder(int orderId);

}
