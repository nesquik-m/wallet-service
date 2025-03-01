package com.example.mapper;

import com.example.dto.request.WalletTransactionRequest;
import com.example.enums.TransactionStatus;
import com.example.model.Transaction;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class TransactionMapper {

    public Transaction mapToTransaction(WalletTransactionRequest request) {
        return Transaction.builder()
                .walletId(request.getWalletId())
                .operationType(request.getOperationType())
                .amount(request.getAmount())
                .status(TransactionStatus.PENDING)
                .build();
    }

}