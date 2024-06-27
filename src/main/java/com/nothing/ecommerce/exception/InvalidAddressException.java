package com.nothing.ecommerce.exception;

import lombok.NoArgsConstructor;

@NoArgsConstructor
public class InvalidAddressException extends UserException {
    public InvalidAddressException(String message) {
        super(message);
    }
}
