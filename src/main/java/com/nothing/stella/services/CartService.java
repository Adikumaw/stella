package com.nothing.stella.services;

import java.util.List;

import com.nothing.stella.entity.Cart;
import com.nothing.stella.entity.CartItem;
import com.nothing.stella.model.CartUpdateRequest;
import com.nothing.stella.model.CartViewModel;
import com.nothing.stella.model.ProductOrderRequest;

public interface CartService {
    List<CartViewModel> create(String reference, ProductOrderRequest orderRequest);

    List<CartViewModel> AddToCart(String reference, int cartId, ProductOrderRequest request);

    List<CartViewModel> fetchCarts(String reference);

    List<CartViewModel> fetchCarts(int userId);

    List<CartViewModel> update(String reference, CartUpdateRequest request);

    List<CartViewModel> deleteCart(String reference, int cartId);

    // ----------------------------------------------------------------
    // BASIC METHODS
    // ----------------------------------------------------------------
    Cart getCart(int cartId);

    List<CartItem> getCartItems(int cartId);

    void delete(CartItem cartItem);

    void delete(Cart cart);

}
