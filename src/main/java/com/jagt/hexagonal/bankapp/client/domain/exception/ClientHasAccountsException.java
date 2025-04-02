package com.jagt.hexagonal.bankapp.client.domain.exception;

public class ClientHasAccountsException extends RuntimeException {
    public ClientHasAccountsException(String message) {
        super(message);
    }
}
