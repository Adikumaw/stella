package com.nothing.stella.exception;

import lombok.NoArgsConstructor;

@NoArgsConstructor
public class InvalidJWTHeaderException extends JwtException {
    public InvalidJWTHeaderException(String message) {
        super(message);
    }
}
