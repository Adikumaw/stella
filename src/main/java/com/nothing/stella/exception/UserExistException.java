package com.nothing.stella.exception;

import lombok.NoArgsConstructor;

@NoArgsConstructor
public class UserExistException extends UserException {
    public UserExistException(String message) {
        super(message);
    }
}
