package com.example.service;

import com.example.model.Wallet;

import java.math.BigDecimal;
import java.util.UUID;

public interface WalletService {

    Wallet getWalletById(UUID walletId);

    void deposit(Wallet wallet, BigDecimal amount);

    void withdraw(Wallet wallet, BigDecimal amount);

}