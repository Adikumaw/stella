package com.nothing.ecommerce.exception;

public class UserNotFoundException extends UserException {

    public UserNotFoundException() {
    }

    public UserNotFoundException(String message) {
        super(message);
    }
}
