package com.nothing.stella.exception;

import lombok.NoArgsConstructor;

@NoArgsConstructor
public class WrongPasswordException extends UserException {
    public WrongPasswordException(String message) {
        super(message);
    }
}
