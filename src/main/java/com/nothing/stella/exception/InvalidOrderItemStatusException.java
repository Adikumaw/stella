package com.nothing.stella.exception;

import lombok.NoArgsConstructor;

@NoArgsConstructor
public class InvalidOrderItemStatusException extends OrderException {
    public InvalidOrderItemStatusException(String message) {
        super(message);
    }
}
