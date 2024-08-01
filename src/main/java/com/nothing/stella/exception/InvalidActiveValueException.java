package com.nothing.stella.exception;

import lombok.NoArgsConstructor;

@NoArgsConstructor
public class InvalidActiveValueException extends ProductException {
    public InvalidActiveValueException(String message) {
        super(message);
    }
}
