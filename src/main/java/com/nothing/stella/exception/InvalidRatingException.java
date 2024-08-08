package com.nothing.stella.exception;

import lombok.NoArgsConstructor;

@NoArgsConstructor
public class InvalidRatingException extends UserException {
    public InvalidRatingException(String message) {
        super(message);
    }
}
