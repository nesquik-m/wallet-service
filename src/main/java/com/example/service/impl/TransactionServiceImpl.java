package com.example.service.impl;

import com.example.enums.OperationType;
import com.example.enums.TransactionStatus;
import com.example.exception.NotEnoughBalanceException;
import com.example.model.Transaction;
import com.example.model.Wallet;
import com.example.repository.TransactionRepository;
import com.example.service.TransactionService;
import com.example.service.WalletService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Slf4j
public class TransactionServiceImpl implements TransactionService {

    private final TransactionRepository transactionRepository;

    private final WalletService walletService;

    @Override
    @Transactional(noRollbackFor = NotEnoughBalanceException.class)
    public void createTransaction(Transaction transaction) {
        transactionRepository.save(transaction);
        try {
            if (transaction.getOperationType().equals(OperationType.DEPOSIT)) {
                handleDepositOperation(transaction, transaction.getWallet());
            } else {
                handleWithdrawOperation(transaction, transaction.getWallet());
            }
        } finally {
            transactionRepository.save(transaction);
        }
    }

    private void handleDepositOperation(Transaction transaction, Wallet wallet) {
        walletService.deposit(wallet, transaction.getAmount());
        transaction.setStatus(TransactionStatus.APPROVED);
    }

    private void handleWithdrawOperation(Transaction transaction, Wallet wallet) {
        try {
            walletService.withdraw(wallet, transaction.getAmount());
            transaction.setStatus(TransactionStatus.APPROVED);
        } catch (NotEnoughBalanceException e) {
            transaction.setStatus(TransactionStatus.FAILED);
            throw e;
        }
    }

}