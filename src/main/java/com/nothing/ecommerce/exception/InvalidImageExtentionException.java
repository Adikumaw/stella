package com.nothing.ecommerce.exception;

import lombok.NoArgsConstructor;

@NoArgsConstructor
public class InvalidImageExtentionException extends ImageException {
    public InvalidImageExtentionException(String message) {
        super(message);
    }
}
