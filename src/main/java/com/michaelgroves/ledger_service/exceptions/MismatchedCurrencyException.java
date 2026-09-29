package com.michaelgroves.ledger_service.exceptions;

public class MismatchedCurrencyException extends RuntimeException {
  public MismatchedCurrencyException(String message) {
    super(message);
  }
}
