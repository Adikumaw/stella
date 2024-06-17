package com.nothing.ecommerce.exception;

import lombok.NoArgsConstructor;

@NoArgsConstructor
public class InvalidEmailException extends UserException {
    public InvalidEmailException(String message) {
        super(message);
    }
}
