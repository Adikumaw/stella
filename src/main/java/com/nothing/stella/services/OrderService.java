package com.nothing.stella.services;

import com.nothing.stella.model.OrderRequest;
import com.nothing.stella.model.OrderViewModel;
import com.nothing.stella.model.PaymentCallbackRequest;
import com.nothing.stella.model.SellerOrderViewModel;

import java.util.List;

import com.nothing.stella.entity.Order;
import com.nothing.stella.model.OrderPaymentRequest;

public interface OrderService {

    // --------------------------------------------------------
    // Functions for Buyer
    // --------------------------------------------------------
    OrderPaymentRequest create(String reference, OrderRequest orderRequest);

    OrderPaymentRequest create(String reference, int addressId, int cartId);

    void handlePaymentCallback(PaymentCallbackRequest request);

    List<OrderViewModel> fetchOrders(String reference);

    String fetchOrderStatus(int orderId);

    Order checkRazorpayStatus(Order order);

    void emailSender(int userId, OrderViewModel orderViewModel);

    OrderViewModel fetchOrder(int orderId);

    // ----------------------------------------------------------------
    // Functions for SellerDashBoard
    // ----------------------------------------------------------------
    List<SellerOrderViewModel> fetchAllOrders(int sellerId);

    Boolean verifySellerAccessByOrderItemId(int sellerId, int orderItemId);

    void updateOrderItemStatusToAccepted(int orderItemId);

    void updateOrderItemStatusToCanceled(int orderItemId);

    void updateOrderItemStatusToShipped(int orderItemId);

}
