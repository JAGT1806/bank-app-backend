package com.jagt.hexagonal.bankapp.client.domain.exception;

public class InvalidBirthdayException extends RuntimeException {
    public InvalidBirthdayException(String message) {
        super(message);
    }
}
