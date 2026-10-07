package com.michaelgroves.ledger_service.persistence;

import com.michaelgroves.ledger_service.domain.Account;
import com.michaelgroves.ledger_service.domain.AccountType;
import org.springframework.jdbc.core.simple.JdbcClient;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public class AccountRepository {

    private final JdbcClient jdbc;

    public AccountRepository(JdbcClient jdbc) {
        this.jdbc = jdbc;
    }

    public void save(Account account) {
        jdbc.sql("INSERT INTO accounts (id, type) VALUES (:id, :type)")
                .param("id", account.id())
                .param("type", account.accountType().name())
                .update();
    }

    public Optional<Account> findById(String id) {
        return jdbc.sql("SELECT id, type FROM accounts where id = :id")
                .param("id", id)
                .query((rs, rowNum) ->
                        new Account(rs.getString("id"), AccountType.valueOf(rs.getString("type"))))
                .optional();
    }


}
