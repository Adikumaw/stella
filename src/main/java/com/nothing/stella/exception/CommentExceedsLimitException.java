package com.nothing.stella.exception;

import lombok.NoArgsConstructor;

@NoArgsConstructor
public class CommentExceedsLimitException extends UserException {
    public CommentExceedsLimitException(String message) {
        super(message);
    }
}
