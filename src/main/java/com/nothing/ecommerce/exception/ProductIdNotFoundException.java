package com.nothing.ecommerce.exception;

import lombok.NoArgsConstructor;

@NoArgsConstructor
public class ProductIdNotFoundException extends OrderException {
    public ProductIdNotFoundException(String message) {
        super(message);
    }
}
