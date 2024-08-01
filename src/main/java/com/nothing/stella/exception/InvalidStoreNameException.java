package com.nothing.stella.exception;

import lombok.NoArgsConstructor;

@NoArgsConstructor
public class InvalidStoreNameException extends SellerException {
    public InvalidStoreNameException(String message) {
        super(message);
    }
}
