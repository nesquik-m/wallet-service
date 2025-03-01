package com.example.mapper;

import com.example.dto.request.WalletTransactionRequest;
import com.example.enums.TransactionStatus;
import com.example.model.Transaction;
import com.example.service.WalletService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class TransactionMapper {

    private final WalletService walletService;

    public Transaction mapToTransaction(WalletTransactionRequest request) {
        return Transaction.builder()
                .wallet(walletService.getWalletById(request.getWalletId())
                        .orElseGet(() -> walletService.createWallet(request.getWalletId())))
                .operationType(request.getOperationType())
                .amount(request.getAmount())
                .status(TransactionStatus.PENDING)
                .build();
    }

}