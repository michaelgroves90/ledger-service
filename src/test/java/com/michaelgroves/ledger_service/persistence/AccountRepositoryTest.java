package com.michaelgroves.ledger_service.persistence;

import com.michaelgroves.ledger_service.TestcontainersConfiguration;
import com.michaelgroves.ledger_service.domain.Account;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.jdbc.test.autoconfigure.AutoConfigureTestDatabase;
import org.springframework.boot.jdbc.test.autoconfigure.JdbcTest;
import org.springframework.context.annotation.Import;
import org.springframework.dao.DuplicateKeyException;

import java.util.Optional;

import static com.michaelgroves.ledger_service.domain.AccountType.ASSET;
import static com.michaelgroves.ledger_service.domain.AccountType.LIABILITY;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

@JdbcTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@Import({TestcontainersConfiguration.class, AccountRepository.class})
public class AccountRepositoryTest {

    @Autowired
    private AccountRepository accounts;

    @Test
    void findAccountThatWasSaved() {
        Account account = new Account("cash-jpy", ASSET);
        accounts.save(account);
        assertEquals(Optional.of(account), accounts.findById("cash-jpy"));
    }

    @Test
    void findNoAccountForAnUnknownId() {
        Account account = new Account("cash-usd", ASSET);
        accounts.save(account);
        assertEquals(Optional.empty(), accounts.findById("cash-jpy"));
    }

    @Test
    void duplicateAccountIdsCannotBeSaved() {
        Account accountOne = new Account("cash-usd", ASSET);
        Account accountTwo = new Account("cash-usd", LIABILITY);
        accounts.save(accountOne);
        assertThrows(DuplicateKeyException.class, () -> accounts.save(accountTwo));
    }

}

