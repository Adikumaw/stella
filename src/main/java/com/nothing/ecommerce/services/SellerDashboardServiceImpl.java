package com.nothing.ecommerce.services;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.nothing.ecommerce.model.ProductIdAndNameModel;
import com.nothing.ecommerce.model.SellerOrderViewModel;

@Service
public class SellerDashboardServiceImpl implements SellerDashboardService {

    @Autowired
    private UserService userService;
    @Autowired
    private ProductService productService;
    @Autowired
    private OrderService orderService;

    @Override
    public List<SellerOrderViewModel> fetchOrders(String reference) {
        int userId = userService.findUserIdByReference(reference);

        List<ProductIdAndNameModel> products = productService.findProductIdAndNameByUserId(userId);

        List<SellerOrderViewModel> sellerOrders = new ArrayList<SellerOrderViewModel>();

        for (ProductIdAndNameModel product : products) {
            List<SellerOrderViewModel> orders = orderService.fetchOrdersByProductId(product.getProductId(),
                    product.getName());

            sellerOrders.addAll(orders);
        }

        return sellerOrders;
    }

}
