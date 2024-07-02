package com.nothing.ecommerce.exception;

import lombok.NoArgsConstructor;

@NoArgsConstructor
public class InvalidOldAddressException extends UserException {
    public InvalidOldAddressException(String message) {
        super(message);
    }
}
