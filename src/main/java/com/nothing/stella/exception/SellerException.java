package com.nothing.stella.exception;

import lombok.NoArgsConstructor;

@NoArgsConstructor
public class SellerException extends RuntimeException {
    public SellerException(String message) {
        super(message);
    }
}
