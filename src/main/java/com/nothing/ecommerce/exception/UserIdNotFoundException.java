package com.nothing.ecommerce.exception;

import lombok.NoArgsConstructor;

@NoArgsConstructor
public class UserIdNotFoundException extends ProductException {
    public UserIdNotFoundException(String message) {
        super(message);
    }
}
