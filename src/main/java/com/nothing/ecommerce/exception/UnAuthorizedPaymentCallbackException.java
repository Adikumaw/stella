package com.nothing.ecommerce.exception;

import lombok.NoArgsConstructor;

@NoArgsConstructor
public class UnAuthorizedPaymentCallbackException extends OrderException {
    public UnAuthorizedPaymentCallbackException(String message) {
        super(message);
    }
}
