CREATE TABLE accounts (
    id      TEXT PRIMARY KEY CHECK (id ~ '^[a-z0-9]+(-[a-z0-9]+)*$'),
    type    TEXT NOT NULL CHECK (type IN ('ASSET', 'EXPENSE', 'INCOME', 'EQUITY', 'LIABILITY'))
);