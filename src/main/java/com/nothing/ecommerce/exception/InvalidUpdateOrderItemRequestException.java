package com.nothing.ecommerce.exception;

import lombok.NoArgsConstructor;

@NoArgsConstructor
public class InvalidUpdateOrderItemRequestException extends OrderException {
    public InvalidUpdateOrderItemRequestException(String message) {
        super(message);
    }
}
