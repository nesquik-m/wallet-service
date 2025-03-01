package com.example.service.impl;

import com.example.exception.NotEnoughBalanceException;
import com.example.model.Wallet;
import com.example.repository.WalletRepository;
import com.example.service.WalletService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class WalletServiceImpl implements WalletService {

    private final WalletRepository walletRepository;

    @Override
    public Optional<Wallet> getWalletById(UUID walletId) {
        return walletRepository.findById(walletId);
    }

    @Override
    public Wallet createWallet(UUID walletId) {
        return walletRepository.save(new Wallet(walletId, BigDecimal.ZERO));
    }

    @Override
    public void deposit(Wallet wallet, BigDecimal amount) {
        Wallet lockedWallet = walletRepository.findByIdForUpdate(wallet.getId());
        lockedWallet.setBalance(lockedWallet.getBalance().add(amount));
        walletRepository.save(lockedWallet);
    }

    @Override
    public void withdraw(Wallet wallet, BigDecimal amount) {
        Wallet lockedWallet = walletRepository.findByIdForUpdate(wallet.getId());

        if (lockedWallet.getBalance().compareTo(amount) < 0) {
            throw new NotEnoughBalanceException(
                    String.format("Not enough balance in wallet with ID %s to withdraw amount: %s.",
                    lockedWallet.getId(), amount));
        }

        lockedWallet.setBalance(lockedWallet.getBalance().subtract(amount));
        walletRepository.save(lockedWallet);
    }

}