package com.nothing.stella.exception;

import lombok.NoArgsConstructor;

@NoArgsConstructor
public class InvalidImageExtentionException extends ImageException {
    public InvalidImageExtentionException(String message) {
        super(message);
    }
}
