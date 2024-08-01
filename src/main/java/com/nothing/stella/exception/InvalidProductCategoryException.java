package com.nothing.stella.exception;

import lombok.NoArgsConstructor;

@NoArgsConstructor
public class InvalidProductCategoryException extends ProductException {
    public InvalidProductCategoryException(String message) {
        super(message);
    }
}
