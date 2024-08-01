package com.nothing.stella.exception;

import lombok.NoArgsConstructor;

@NoArgsConstructor
public class InvalidProductException extends ProductException {
    public InvalidProductException(String message) {
        super(message);
    }
}
