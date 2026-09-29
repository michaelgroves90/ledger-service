package com.michaelgroves.ledger_service.exceptions;

public class InvalidPostingException extends RuntimeException {
    public InvalidPostingException(String message) {
        super(message);
    }
}
