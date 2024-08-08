package com.nothing.stella.exception;

import lombok.NoArgsConstructor;

@NoArgsConstructor
public class ReviewNotAllowedException extends UserException {
    public ReviewNotAllowedException(String message) {
        super(message);
    }
}
