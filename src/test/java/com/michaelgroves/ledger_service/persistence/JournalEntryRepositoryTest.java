package com.michaelgroves.ledger_service.persistence;

import com.michaelgroves.ledger_service.TestcontainersConfiguration;
import com.michaelgroves.ledger_service.domain.*;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.jdbc.test.autoconfigure.AutoConfigureTestDatabase;
import org.springframework.boot.jdbc.test.autoconfigure.JdbcTest;
import org.springframework.context.annotation.Import;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.jdbc.core.simple.JdbcClient;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

import static com.michaelgroves.ledger_service.domain.AccountType.ASSET;
import static com.michaelgroves.ledger_service.domain.AccountType.INCOME;
import static com.michaelgroves.ledger_service.domain.Currency.JPY;
import static com.michaelgroves.ledger_service.domain.Direction.CREDIT;
import static com.michaelgroves.ledger_service.domain.Direction.DEBIT;
import static org.junit.jupiter.api.Assertions.*;

@JdbcTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@Import({TestcontainersConfiguration.class, JournalEntryRepository.class, AccountRepository.class})
public class JournalEntryRepositoryTest {

    @Autowired
    private JournalEntryRepository journalEntries;
    @Autowired
    private AccountRepository accounts;
    @Autowired
    private JdbcClient jdbc;

    @AfterEach
    void cleanUp() {
        jdbc.sql("DELETE FROM postings").update();
        jdbc.sql("DELETE FROM journal_entries").update();
        jdbc.sql("DELETE FROM accounts").update();
    }

    @Test
    void savesAnEntryAndItsPostings() {
        accounts.save(new Account("cash-jpy", ASSET));
        accounts.save(new Account("sales-jpy", INCOME));
        Posting postingOne = new Posting("cash-jpy", new Money(JPY, 1000), DEBIT);
        Posting postingTwo = new Posting("sales-jpy", new Money(JPY, 1000), CREDIT);
        JournalEntry journalEntry = new JournalEntry("Sale, Order 123", List.of(postingOne, postingTwo));

        long entryId = journalEntries.save(journalEntry, "key-1");
        long entryRows = jdbc.sql(
                "SELECT COUNT(*) FROM journal_entries WHERE id = :id")
                .param("id", entryId)
                .query(Long.class)
                .single();

        long postingRows = jdbc.sql(
                "SELECT COUNT(*) FROM postings WHERE journal_entry_id = :id")
                .param("id", entryId)
                .query(Long.class)
                .single();
        assertEquals(1, entryRows);
        assertEquals(2, postingRows);
    }

    @Test
    // Runs test without a transaction for purposes of testing an 'invalid' transaction
    @Transactional(propagation = Propagation.NOT_SUPPORTED)
    void failingToSaveAllSavesNone() {
        accounts.save(new Account("cash-jpy", ASSET));
        Posting postingOne = new Posting("cash-jpy", new Money(JPY, 1000), DEBIT);
        Posting postingTwo = new Posting("bad-id", new Money(JPY, 1000), CREDIT);
        JournalEntry journalEntry = new JournalEntry("Sale, Order 123", List.of(postingOne, postingTwo));

        assertThrows(DataIntegrityViolationException.class, () -> journalEntries.save(journalEntry, "key-1"));

        long entryRows = jdbc.sql(
                        "SELECT COUNT(*) FROM journal_entries WHERE idempotency_key = :key")
                .param("key", "key-1")
                .query(Long.class)
                .single();

        long postingRows = jdbc.sql(
                        "SELECT COUNT(*) FROM postings WHERE account_id = :account")
                .param("account", "cash-jpy")
                .query(Long.class)
                .single();

        assertEquals(0, entryRows);
        assertEquals(0, postingRows);
    }

    @Test
    void findsEntryById() {
        accounts.save(new Account("cash-jpy", ASSET));
        accounts.save(new Account("sales-jpy", INCOME));
        Posting postingOne = new Posting("cash-jpy", new Money(JPY, 1000), DEBIT);
        Posting postingTwo = new Posting("sales-jpy", new Money(JPY, 1000), CREDIT);
        JournalEntry journalEntry = new JournalEntry("Sale, Order 123", List.of(postingOne, postingTwo));

        long entryId = journalEntries.save(journalEntry, "key-1");

        PostedJournalEntry postedEntry = journalEntries.findById(entryId).orElseThrow();

        assertEquals(journalEntry, postedEntry.entry());
        assertEquals(entryId, postedEntry.id());
        assertNotNull(postedEntry.createdAt());

    }

    @Test
    void idThatDoesNotExistReturnsEmptyOptional() {
        accounts.save(new Account("cash-jpy", ASSET));
        accounts.save(new Account("sales-jpy", INCOME));
        Posting postingOne = new Posting("cash-jpy", new Money(JPY, 1000), DEBIT);
        Posting postingTwo = new Posting("sales-jpy", new Money(JPY, 1000), CREDIT);
        long entryId = journalEntries.save(new JournalEntry("blah", List.of(postingOne, postingTwo)), "key-1");

        Optional<PostedJournalEntry > postedEntry = journalEntries.findById(entryId + 1);

        assertEquals(Optional.empty(), postedEntry);

    }


}
