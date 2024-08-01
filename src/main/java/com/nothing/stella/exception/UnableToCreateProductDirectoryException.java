package com.nothing.stella.exception;

import lombok.NoArgsConstructor;

@NoArgsConstructor
public class UnableToCreateProductDirectoryException extends ImageException {
    public UnableToCreateProductDirectoryException(String message) {
        super(message);
    }
}
