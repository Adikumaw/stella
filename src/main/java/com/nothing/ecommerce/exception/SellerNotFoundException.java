package com.nothing.ecommerce.exception;

import lombok.NoArgsConstructor;

@NoArgsConstructor
public class SellerNotFoundException extends SellerException {
    public SellerNotFoundException(String message) {
        super(message);
    }
}
