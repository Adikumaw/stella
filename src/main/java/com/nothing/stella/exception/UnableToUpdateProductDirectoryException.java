package com.nothing.stella.exception;

import lombok.NoArgsConstructor;

@NoArgsConstructor
public class UnableToUpdateProductDirectoryException extends ImageException {
    public UnableToUpdateProductDirectoryException(String message) {
        super(message);
    }
}
