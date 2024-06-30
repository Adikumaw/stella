package com.nothing.ecommerce.exception;

import lombok.NoArgsConstructor;

@NoArgsConstructor
public class JwtException extends RuntimeException {
    public JwtException(String message) {
        super(message);
    }
}
