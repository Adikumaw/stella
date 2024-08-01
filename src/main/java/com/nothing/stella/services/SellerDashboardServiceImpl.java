package com.nothing.stella.services;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.nothing.stella.exception.InvalidOrderItemStatusException;
import com.nothing.stella.exception.UnAuthorizedUserException;
import com.nothing.stella.model.SellerOrderViewModel;
import com.nothing.stella.model.UpdateOrderItemStatusModal;

@Service
public class SellerDashboardServiceImpl implements SellerDashboardService {

    @Autowired
    private UserService userService;
    @Autowired
    private OrderService orderService;

    @Override
    public List<SellerOrderViewModel> fetchOrders(String reference) {
        int sellerId = userService.findUserIdByReference(reference);

        return orderService.fetchAllOrders(sellerId);
    }

    @Override
    public List<SellerOrderViewModel> updateOrderItemStatus(String reference, UpdateOrderItemStatusModal request) {
        int sellerId = userService.findUserIdByReference(reference);

        Boolean verifySeller = orderService.verifySellerAccessByOrderItemId(sellerId, request.getOrder_id());

        if (verifySeller) {

            switch (request.getStatus()) {
                case "accepted":
                    orderService.updateOrderItemStatusToAccepted(request.getOrder_id());
                    break;
                case "canceled":
                    orderService.updateOrderItemStatusToCanceled(request.getOrder_id());
                    break;
                case "shipped":
                    orderService.updateOrderItemStatusToShipped(request.getOrder_id());
                    break;
                default:
                    throw new InvalidOrderItemStatusException("Error: Invalid order item status");
            }

            return orderService.fetchAllOrders(sellerId);

        } else {
            throw new UnAuthorizedUserException(
                    "Error: Access denied!, you are not allowed to update this order status");
        }
    }

}
