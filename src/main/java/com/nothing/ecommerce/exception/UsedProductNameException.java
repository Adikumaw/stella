package com.nothing.ecommerce.exception;

import lombok.NoArgsConstructor;

@NoArgsConstructor
public class UsedProductNameException extends ProductException {
    public UsedProductNameException(String message) {
        super(message);
    }
}
