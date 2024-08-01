package com.nothing.stella.services;

import java.util.List;

import com.nothing.stella.model.SellerOrderViewModel;
import com.nothing.stella.model.UpdateOrderItemStatusModal;

public interface SellerDashboardService {
    List<SellerOrderViewModel> fetchOrders(String reference);

    List<SellerOrderViewModel> updateOrderItemStatus(String reference, UpdateOrderItemStatusModal request);
}
