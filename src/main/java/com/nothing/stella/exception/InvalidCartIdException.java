package com.nothing.stella.exception;

import lombok.NoArgsConstructor;

@NoArgsConstructor
public class InvalidCartIdException extends UserException {
    public InvalidCartIdException(String message) {
        super(message);
    }
}
