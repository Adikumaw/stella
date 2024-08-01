package com.nothing.stella.controller;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.nothing.stella.exception.InvalidJWTHeaderException;
import com.nothing.stella.exception.OrderException;
import com.nothing.stella.exception.ProductException;
import com.nothing.stella.exception.UnknownErrorException;
import com.nothing.stella.exception.UserException;
import com.nothing.stella.model.OrderRequest;
import com.nothing.stella.model.OrderViewModel;
import com.nothing.stella.model.PaymentCallbackRequest;
import com.nothing.stella.model.OrderPaymentRequest;
import com.nothing.stella.services.JWTService;
import com.nothing.stella.services.OrderService;

@RestController
@RequestMapping("/api/orders")
@CrossOrigin(origins = "*")
public class OrderController {
    @Autowired
    private OrderService orderService;
    @Autowired
    private JWTService jwtService;

    private static final Logger logger = LoggerFactory.getLogger(OrderController.class);

    @PostMapping
    public OrderPaymentRequest createOrder(@RequestBody OrderRequest orderRequest,
            @RequestHeader("Authorization") String jwtHeader) {
        if (jwtService.verifyJwtHeader(jwtHeader)) {

            // extract token from request header
            String jwtToken = jwtHeader.substring(7);
            try {
                String reference = jwtService.fetchReference(jwtToken);

                return orderService.create(reference, orderRequest);
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

    @PostMapping("/order-by-cart")
    public OrderPaymentRequest createOrder(@RequestBody Integer addressId, @RequestBody Integer cartId,
            @RequestHeader("Authorization") String jwtHeader) {
        if (jwtService.verifyJwtHeader(jwtHeader)) {

            // extract token from request header
            String jwtToken = jwtHeader.substring(7);
            try {
                String reference = jwtService.fetchReference(jwtToken);

                return orderService.create(reference, addressId, cartId);
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

    @PostMapping("/payment-callback")
    public void handlePaymentCallback(@RequestBody PaymentCallbackRequest request) {
        try {
            orderService.handlePaymentCallback(request);
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
    }

    @GetMapping
    public List<OrderViewModel> fetchOrders(@RequestHeader("Authorization") String jwtHeader) {
        if (jwtService.verifyJwtHeader(jwtHeader)) {

            // extract token from request header
            String jwtToken = jwtHeader.substring(7);
            try {
                String reference = jwtService.fetchReference(jwtToken);

                return orderService.fetchOrders(reference);
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

    @GetMapping("/track")
    public String fetchOrderStatus(@RequestParam("order_id") Integer orderId) {
        return orderService.fetchOrderStatus(orderId);
    }

}
