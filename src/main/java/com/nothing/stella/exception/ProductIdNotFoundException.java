package com.nothing.stella.exception;

import lombok.NoArgsConstructor;

@NoArgsConstructor
public class ProductIdNotFoundException extends OrderException {
    public ProductIdNotFoundException(String message) {
        super(message);
    }
}
