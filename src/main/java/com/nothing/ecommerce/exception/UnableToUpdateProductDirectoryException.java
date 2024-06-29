package com.nothing.ecommerce.exception;

import lombok.NoArgsConstructor;

@NoArgsConstructor
public class UnableToUpdateProductDirectoryException extends ImageException {
    public UnableToUpdateProductDirectoryException(String message) {
        super(message);
    }
}
