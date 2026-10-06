package com.michaelgroves.ledger_service.domain;

import com.michaelgroves.ledger_service.exceptions.InvalidAccountException;

import java.util.Objects;
import java.util.regex.Pattern;

public record Account(String id, AccountType accountType) {

    private static final Pattern ID_FORMAT = Pattern.compile("[a-z0-9]+(-[a-z0-9]+)*");

    public Account {
        Objects.requireNonNull(id, "ID cannot be null");
        Objects.requireNonNull(accountType, "Account Type cannot be null");
        if(!ID_FORMAT.matcher(id).matches()) {
            throw new InvalidAccountException(String.format("Invalid ID: '%s' - Use lowercase letters and digits, words separated by single hyphens", id));
        }
    }

}
