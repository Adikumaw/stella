package com.nothing.ecommerce.exception;

import lombok.NoArgsConstructor;

@NoArgsConstructor
public class InvalidRoleException extends UserException {
    public InvalidRoleException(String message) {
        super(message);
    }
}
