package com.nothing.ecommerce.exception;

import lombok.NoArgsConstructor;

@NoArgsConstructor
public class InvalidNumberException extends UserException {
    public InvalidNumberException(String message) {
        super(message);
    }
}
