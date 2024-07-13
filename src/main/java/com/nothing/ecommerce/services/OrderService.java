package com.nothing.ecommerce.services;

import com.nothing.ecommerce.model.OrderRequest;
import com.nothing.ecommerce.model.OrderResponse;

public interface OrderService {
    OrderResponse create(String reference, OrderRequest orderRequest);
}
