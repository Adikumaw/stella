package com.nothing.ecommerce.services;

import java.util.List;

import com.nothing.ecommerce.model.SellerOrderViewModel;
import com.nothing.ecommerce.model.UpdateOrderItemStatusModal;

public interface SellerDashboardService {
    List<SellerOrderViewModel> fetchOrders(String reference);

    List<SellerOrderViewModel> updateOrderItemStatus(String reference, UpdateOrderItemStatusModal request);
}
