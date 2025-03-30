package com.jagt.hexagonal.bankapp.client.domain.exception;

public class AgeRestrictionException extends RuntimeException {
    public AgeRestrictionException(String message) {
        super(message);
    }
}
