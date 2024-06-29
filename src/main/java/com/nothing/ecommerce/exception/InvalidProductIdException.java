package com.nothing.ecommerce.exception;

import lombok.NoArgsConstructor;

@NoArgsConstructor
public class InvalidProductIdException extends ProductException {
    public InvalidProductIdException(String message) {
        super(message);
    }
}
