package com.nothing.ecommerce.exception;

import lombok.NoArgsConstructor;

@NoArgsConstructor
public class IllegalRoleException extends RuntimeException {
    public IllegalRoleException(String message) {
        super(message);
    }
}
