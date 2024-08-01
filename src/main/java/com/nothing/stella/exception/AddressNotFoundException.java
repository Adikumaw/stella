package com.nothing.stella.exception;

import lombok.NoArgsConstructor;

@NoArgsConstructor
public class AddressNotFoundException extends UserException {
    public AddressNotFoundException(String message) {
        super(message);
    }
}
