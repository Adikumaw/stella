package com.nothing.stella.exception;

import lombok.NoArgsConstructor;

@NoArgsConstructor
public class UnknownErrorException extends RuntimeException {
    public UnknownErrorException(String message) {
        super(message);
    }
}
