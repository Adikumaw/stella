package com.nothing.ecommerce.exception;

import lombok.NoArgsConstructor;

@NoArgsConstructor
public class InvalidEmailOrNumberException extends UserException {
    public InvalidEmailOrNumberException(String message) {
        super(message);
    }
}
