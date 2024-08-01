package com.nothing.stella.services;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.nothing.stella.entity.Cart;
import com.nothing.stella.entity.CartItem;
import com.nothing.stella.entity.Product;
import com.nothing.stella.exception.InvalidCartIdException;
import com.nothing.stella.exception.InvalidProductIdException;
import com.nothing.stella.exception.InvalidProductQuantityException;
import com.nothing.stella.exception.UnAuthorizedUserException;
import com.nothing.stella.exception.UnknownErrorException;
import com.nothing.stella.model.CartViewModel;
import com.nothing.stella.model.ProductOrderRequest;
import com.nothing.stella.model.ProductOrderViewModel;
import com.nothing.stella.repository.CartItemRepository;
import com.nothing.stella.repository.CartRepository;

@Service
public class CartServiceImpl implements CartService {
    @Autowired
    private UserService userService;
    @Autowired
    private CartRepository cartRepository;
    @Autowired
    private CartItemRepository cartItemRepository;
    @Autowired
    private ProductService productService;

    @Override
    public List<CartViewModel> create(String reference, ProductOrderRequest request) {
        int userId = userService.findUserIdByReference(reference);

        Double productPrice = 0.0;

        // verify product Id
        if (request.getQuantity() >= 1) {
            int productId = request.getProductId();
            productPrice = productService.findPriceById(productId);
            if (productPrice == 0.0) {
                throw new InvalidProductIdException("Error: Product not found with id " + productId);
            }
        } else {
            throw new InvalidProductQuantityException("Error: Quantity must be one or more");
        }

        Cart cart = null;
        CartItem cartItem = null;
        try {
            // Create a new dummy cart object
            cart = new Cart(userId, 0.0);
            cart = cartRepository.save(cart); // Save the cart object
            int cartId = cart.getCartId(); // Get the cart id

            // create Cart Item in DataBase and find total price
            int productId = request.getProductId();
            int quantity = request.getQuantity();

            cartItem = new CartItem(cartId, productId, quantity, productPrice); // create a new cartItem
            cartItem = cartItemRepository.save(cartItem); // save the cartItem

            Double totalPrice = cartItem.getTotalPrice(); // fetch the total price

            cart.setTotalAmount(totalPrice);
            cart = cartRepository.save(cart); // update cart to DataBase

            return fetchCarts(userId);
        }
        // Delete cart from DataBase any issue occurred
        catch (Exception e) {

            cartItemRepository.delete(cartItem); // delete the cartItem from DataBase
            cartRepository.delete(cart); // delete the cart from DataBase

            throw new UnknownErrorException("Error: error creating Cart {" + e.getMessage() + "}");
        }
    }

    @Override
    public List<CartViewModel> AddToCart(String reference, int cartId, ProductOrderRequest request) {
        int userId = userService.findUserIdByReference(reference);

        Double productPrice = 0.0;

        // verify product Id
        if (request.getQuantity() >= 1) {
            int productId = request.getProductId();
            productPrice = productService.findPriceById(productId);
            if (productPrice == 0.0) {
                throw new InvalidProductIdException("Error: Product not found with id " + productId);
            }
        } else {
            throw new InvalidProductQuantityException("Error: Quantity must be one or more");
        }

        Cart cart = getCart(cartId);

        if (cart != null) {
            if (cart.getUserId() == userId) {
                // create Cart Item in DataBase and find total price
                int productId = request.getProductId();
                int quantity = request.getQuantity();

                CartItem cartItem = new CartItem(cartId, productId, quantity, productPrice); // create a new cartItem
                cartItem = cartItemRepository.save(cartItem); // save the cartItem

                Double totalPrice = cartItem.getTotalPrice(); // fetch the total price

                cart.setTotalAmount(cart.getTotalAmount() + totalPrice);
                cart = cartRepository.save(cart); // update cart to DataBase

                return fetchCarts(userId);

            } else {
                throw new InvalidCartIdException("Error: you are not allowed to access this cart");
            }
        } else {
            throw new InvalidCartIdException("Error: cart not found");
        }
    }

    @Override
    public List<CartViewModel> fetchCarts(String reference) {
        int userId = userService.findUserIdByReference(reference);

        List<CartViewModel> cartViewModels = new ArrayList<CartViewModel>();
        List<Cart> carts = cartRepository.findByUserId(userId);

        for (Cart cart : carts) {
            List<CartItem> cartItems = cartItemRepository.findByCartId(cart.getCartId());

            List<ProductOrderViewModel> products = new ArrayList<ProductOrderViewModel>();

            for (CartItem cartItem : cartItems) {
                Product product = productService.findById(cartItem.getProductId());
                products.add(new ProductOrderViewModel(product, cartItem.getQuantity()));
            }

            cartViewModels.add(new CartViewModel(cart.getCartId(), products));
        }

        return cartViewModels;
    }

    @Override
    public List<CartViewModel> fetchCarts(int userId) {

        List<CartViewModel> cartViewModels = new ArrayList<CartViewModel>();
        List<Cart> carts = cartRepository.findByUserId(userId);

        for (Cart cart : carts) {
            List<CartItem> cartItems = cartItemRepository.findByCartId(cart.getCartId());

            List<ProductOrderViewModel> products = new ArrayList<ProductOrderViewModel>();

            for (CartItem cartItem : cartItems) {
                Product product = productService.findById(cartItem.getProductId());
                products.add(new ProductOrderViewModel(product, cartItem.getQuantity()));
            }

            cartViewModels.add(new CartViewModel(cart.getCartId(), products));
        }

        return cartViewModels;
    }

    @Override
    public List<CartViewModel> deleteCart(String reference, int cartId) {
        int userId = userService.findUserIdByReference(reference);

        Cart cart = getCart(cartId);

        if (cart != null) {
            if (cart.getUserId() == userId) {
                List<CartItem> cartItems = cartItemRepository.findByCartId(cartId);

                // Delete cart along with cartItems
                for (CartItem cartItem : cartItems) {
                    cartItemRepository.delete(cartItem);
                }
                cartRepository.delete(cart);

                return fetchCarts(userId);

            } else {
                throw new UnAuthorizedUserException("Error: you are not allowed to delete this cart");
            }
        } else {
            throw new InvalidCartIdException("Error: cart not found");
        }
    }

    // ----------------------------------------------------------------
    // BASIC METHODS
    // ----------------------------------------------------------------
    @Override
    public Cart getCart(int cartId) {
        return cartRepository.findById(cartId).orElse(null);
    }

    @Override
    public List<CartItem> getCartItems(int cartId) {
        return cartItemRepository.findByCartId(cartId);
    }

    @Override
    public void delete(Cart cart) {
        cartRepository.delete(cart);
    }

    @Override
    public void delete(CartItem cartItem) {
        cartItemRepository.delete(cartItem);
    }

}
