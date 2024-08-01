package com.nothing.stella.exception;

import lombok.NoArgsConstructor;

@NoArgsConstructor
public class InvalidOrderIdException extends OrderException {
    public InvalidOrderIdException(String message) {
        super(message);
    }
}
