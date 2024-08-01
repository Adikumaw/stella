package com.nothing.stella.exception;

import lombok.NoArgsConstructor;

@NoArgsConstructor
public class InvalidProductIdException extends ProductException {
    public InvalidProductIdException(String message) {
        super(message);
    }
}
