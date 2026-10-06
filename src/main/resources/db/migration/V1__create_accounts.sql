CREATE TABLE accounts (
    id      TEXT PRIMARY KEY,
    type    TEXT NOT NULL CHECK (type IN ('ASSET', 'EXPENSE', 'INCOME', 'EQUITY', 'LIABILITY'))
);