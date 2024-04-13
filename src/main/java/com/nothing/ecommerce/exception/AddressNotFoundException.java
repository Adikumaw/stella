package com.nothing.ecommerce.exception;

import lombok.NoArgsConstructor;

@NoArgsConstructor
public class AddressNotFoundException extends RuntimeException {
    public AddressNotFoundException(String message) {
        super(message);
    }
}
