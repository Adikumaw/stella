package com.nothing.ecommerce.exception;

import lombok.NoArgsConstructor;

@NoArgsConstructor
public class WeakPasswordException extends UserException {
    public WeakPasswordException(String message) {
        super(message);
    }
}
