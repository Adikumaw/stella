package com.nothing.stella.exception;

import lombok.NoArgsConstructor;

@NoArgsConstructor
public class InvalidPasswordException extends UserException {
    public InvalidPasswordException(String message) {
        super(message);
    }
}
