package com.nothing.stella.exception;

import lombok.NoArgsConstructor;

@NoArgsConstructor
public class EmptyImagesException extends ImageException {
    public EmptyImagesException(String message) {
        super(message);
    }
}
