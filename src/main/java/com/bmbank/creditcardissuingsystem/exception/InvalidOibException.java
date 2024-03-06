package com.bmbank.creditcardissuingsystem.exception;

public class InvalidOibException extends RuntimeException {

    public InvalidOibException(String message, Throwable cause) {
        super(message, cause);
    }

    public InvalidOibException(String message) {
        super(message);
    }
}
