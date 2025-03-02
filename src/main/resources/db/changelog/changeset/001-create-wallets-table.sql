-- liquibase formatted sql

-- changeset Anastasia Martova:create-wallets-table
CREATE TABLE wallets
(
    id     UUID PRIMARY KEY,
    balance DECIMAL NOT NULL
);