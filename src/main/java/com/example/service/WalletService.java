package com.example.service;

import com.example.model.Wallet;

import java.math.BigDecimal;
import java.util.Optional;
import java.util.UUID;

public interface WalletService {

    Optional<Wallet> getWalletById(UUID walletId);

    Wallet getWallet(UUID walletId);

    Wallet createWallet(UUID walletId);

    void deposit(Wallet wallet, BigDecimal amount);

    void withdraw(Wallet wallet, BigDecimal amount);

}