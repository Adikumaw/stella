package com.nothing.ecommerce.controller;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.nothing.ecommerce.exception.InvalidJWTHeaderException;
import com.nothing.ecommerce.exception.OrderException;
import com.nothing.ecommerce.exception.ProductException;
import com.nothing.ecommerce.exception.UnknownErrorException;
import com.nothing.ecommerce.exception.UserException;
import com.nothing.ecommerce.model.SellerOrderViewModel;
import com.nothing.ecommerce.services.JWTService;
import com.nothing.ecommerce.services.SellerDashboardService;

@RestController
@RequestMapping("/sellers/dashboard")
public class SellerDashboardController {

    @Autowired
    private JWTService jwtService;
    @Autowired
    private SellerDashboardService sellerDashboardService;

    private static final Logger logger = LoggerFactory.getLogger(SellerDashboardController.class);

    @GetMapping
    public List<SellerOrderViewModel> getSellerOrderViewModels(@RequestHeader("Authorization") String jwtHeader) {
        if (jwtService.verifyJwtHeader(jwtHeader)) {

            // extract token from request header
            String jwtToken = jwtHeader.substring(7);
            try {
                String reference = jwtService.fetchReference(jwtToken);

                return sellerDashboardService.fetchOrders(reference);
            } catch (UserException e) {
                throw e;
            } catch (ProductException e) {
                throw e;
            } catch (OrderException e) {
                throw e;
            } catch (Exception e) {
                logger.error("Unknown error: " + e.getMessage(), e);
                throw new UnknownErrorException("Error: unknown error");
            }
        } else {
            throw new InvalidJWTHeaderException("Error: Invalid JWTHeader");
        }
    }
}
