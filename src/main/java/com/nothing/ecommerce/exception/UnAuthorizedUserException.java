package com.nothing.ecommerce.exception;

import lombok.NoArgsConstructor;

@NoArgsConstructor
public class UnAuthorizedUserException extends UserException {
    public UnAuthorizedUserException(String message) {
        super(message);
    }
}
