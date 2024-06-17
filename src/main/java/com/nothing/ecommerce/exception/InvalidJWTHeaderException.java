package com.nothing.ecommerce.exception;

import lombok.NoArgsConstructor;

@NoArgsConstructor
public class InvalidJWTHeaderException extends RuntimeException {
    public InvalidJWTHeaderException(String message) {
        super(message);
    }
}
