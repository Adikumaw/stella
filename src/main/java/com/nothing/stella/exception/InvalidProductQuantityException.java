package com.nothing.stella.exception;

import lombok.NoArgsConstructor;

@NoArgsConstructor
public class InvalidProductQuantityException extends OrderException {
    public InvalidProductQuantityException(String message) {
        super(message);
    }
}
