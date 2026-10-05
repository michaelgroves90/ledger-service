package com.michaelgroves.ledger_service.domain;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class AccountTypeTest {

    @DisplayName("Accounts should have correct payment Direction")
    @ParameterizedTest(name = "{0} is a {1}")
    @CsvSource({
            "ASSET, DEBIT",
            "EXPENSE, DEBIT",
            "LIABILITY, CREDIT",
            "INCOME, CREDIT",
            "EQUITY, CREDIT"
    })
    void accountShouldHaveCorrectDirection(AccountType accountType, Direction expectedNormalBalance) {
        assertEquals(expectedNormalBalance, accountType.normalBalance());
    }
}
