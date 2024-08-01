package com.nothing.stella.exception;

import lombok.NoArgsConstructor;

@NoArgsConstructor
public class OrderException extends RuntimeException {
    public OrderException(String message) {
        super(message);
    }
}
