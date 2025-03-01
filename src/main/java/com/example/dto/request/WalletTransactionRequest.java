package com.example.dto.request;

import com.example.enums.OperationType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class WalletTransactionRequest {

    private UUID walletId;

    private OperationType operationType;

    private BigDecimal amount;

}