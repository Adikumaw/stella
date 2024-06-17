package com.nothing.ecommerce.exception;

import lombok.NoArgsConstructor;

@NoArgsConstructor
public class WrongPasswordException extends UserException {
    public WrongPasswordException(String message) {
        super(message);
    }
}
