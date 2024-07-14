package com.nothing.ecommerce.services;

import com.nothing.ecommerce.model.OrderRequest;
import com.nothing.ecommerce.model.PaymentCallbackRequest;

import com.nothing.ecommerce.model.OrderPaymentRequest;

public interface OrderService {
    OrderPaymentRequest create(String reference, OrderRequest orderRequest);

    void handlePaymentCallback(PaymentCallbackRequest request);

}
