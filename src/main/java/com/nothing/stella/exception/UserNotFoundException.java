package com.nothing.stella.exception;

public class UserNotFoundException extends UserException {

    public UserNotFoundException() {
    }

    public UserNotFoundException(String message) {
        super(message);
    }
}
