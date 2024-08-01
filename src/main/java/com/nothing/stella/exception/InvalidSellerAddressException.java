package com.nothing.stella.exception;

import lombok.NoArgsConstructor;

@NoArgsConstructor
public class InvalidSellerAddressException extends SellerException {
    public InvalidSellerAddressException(String message) {
        super(message);
    }
}
