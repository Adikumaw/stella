package com.nothing.stella.exception;

import lombok.NoArgsConstructor;

@NoArgsConstructor
public class ProductExistsInCartException extends UserException {
    public ProductExistsInCartException(String message) {
        super(message);
    }
}
