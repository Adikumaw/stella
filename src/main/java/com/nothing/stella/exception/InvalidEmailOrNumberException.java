package com.nothing.stella.exception;

import lombok.NoArgsConstructor;

@NoArgsConstructor
public class InvalidEmailOrNumberException extends UserException {
    public InvalidEmailOrNumberException(String message) {
        super(message);
    }
}
