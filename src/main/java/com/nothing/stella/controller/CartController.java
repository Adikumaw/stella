package com.nothing.stella.controller;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.nothing.stella.exception.InvalidJWTHeaderException;
import com.nothing.stella.exception.ProductException;
import com.nothing.stella.exception.UnknownErrorException;
import com.nothing.stella.exception.UserException;
import com.nothing.stella.model.AddToCartModel;
import com.nothing.stella.model.CartUpdateRequest;
import com.nothing.stella.model.CartViewModel;
import com.nothing.stella.services.CartService;
import com.nothing.stella.services.JWTService;

@RestController
@RequestMapping("/users/cart")
public class CartController {
    @Autowired
    private JWTService jwtService;
    @Autowired
    private CartService cartService;

    private static final Logger logger = LoggerFactory.getLogger(CartController.class);

    @PostMapping
    public List<CartViewModel> AddToCart(@RequestBody AddToCartModel request,
            @RequestHeader("Authorization") String jwtHeader) {
        if (jwtService.verifyJwtHeader(jwtHeader)) {

            // extract token from request header
            String jwtToken = jwtHeader.substring(7);
            try {
                String reference = jwtService.fetchReference(jwtToken);

                if (request.getCartId() == 0) {
                    return cartService.create(reference, request.getRequest());
                } else {
                    return cartService.AddToCart(reference, request.getCartId(), request.getRequest());
                }
            } catch (UserException e) {
                throw e;
            } catch (ProductException e) {
                throw e;
            } catch (Exception e) {
                logger.error("Unknown error: " + e.getMessage(), e);
                throw new UnknownErrorException("Error: unknown error");
            }
        } else {
            throw new InvalidJWTHeaderException("Error: Invalid JWTHeader");
        }
    }

    @GetMapping
    public List<CartViewModel> fetchCarts(@RequestHeader("Authorization") String jwtHeader) {
        if (jwtService.verifyJwtHeader(jwtHeader)) {

            // extract token from request header
            String jwtToken = jwtHeader.substring(7);
            try {
                String reference = jwtService.fetchReference(jwtToken);

                return cartService.fetchCarts(reference);
            } catch (UserException e) {
                throw e;
            } catch (ProductException e) {
                throw e;
            } catch (Exception e) {
                logger.error("Unknown error: " + e.getMessage(), e);
                throw new UnknownErrorException("Error: unknown error");
            }
        } else {
            throw new InvalidJWTHeaderException("Error: Invalid JWTHeader");
        }
    }

    @PutMapping
    public List<CartViewModel> updateCart(@RequestBody CartUpdateRequest request,
            @RequestHeader("Authorization") String jwtHeader) {
        if (jwtService.verifyJwtHeader(jwtHeader)) {

            // extract token from request header
            String jwtToken = jwtHeader.substring(7);
            try {
                String reference = jwtService.fetchReference(jwtToken);

                return cartService.update(reference, request);
            } catch (UserException e) {
                throw e;
            } catch (ProductException e) {
                throw e;
            } catch (Exception e) {
                logger.error("Unknown error: " + e.getMessage(), e);
                throw new UnknownErrorException("Error: unknown error");
            }
        } else {
            throw new InvalidJWTHeaderException("Error: Invalid JWTHeader");
        }
    }

    @DeleteMapping
    public List<CartViewModel> deleteCart(@RequestBody int cartId, @RequestHeader("Authorization") String jwtHeader) {
        if (jwtService.verifyJwtHeader(jwtHeader)) {

            // extract token from request header
            String jwtToken = jwtHeader.substring(7);
            try {
                String reference = jwtService.fetchReference(jwtToken);

                return cartService.deleteCart(reference, cartId);
            } catch (UserException e) {
                throw e;
            } catch (ProductException e) {
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
