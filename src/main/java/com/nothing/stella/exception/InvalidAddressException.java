package com.nothing.stella.exception;

import lombok.NoArgsConstructor;

@NoArgsConstructor
public class InvalidAddressException extends UserException {
    public InvalidAddressException(String message) {
        super(message);
    }
}
