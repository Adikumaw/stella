package com.nothing.ecommerce.services;

import java.util.List;

import com.nothing.ecommerce.model.SellerOrderViewModel;

public interface SellerDashboardService {
    List<SellerOrderViewModel> fetchOrders(String reference);
}
