package com.nothing.stella.exception;

import lombok.NoArgsConstructor;

@NoArgsConstructor
public class CartItemNotFoundException extends UserException {
    public CartItemNotFoundException(String message) {
        super(message);
    }
}
