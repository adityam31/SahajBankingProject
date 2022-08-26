package com.bankingapp.utils.exceptions;

public class ValidationException extends RuntimeException {
    public ValidationException(String message) {
        super(message);
    }

    public ValidationException(String message, Object...args) {
        super(String.format(message, args));
    }
}
