package com.example.dto.request;

import com.example.enums.OperationType;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
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
public class TransactionRequest {

    @NotNull(message = "walletId cannot be null")
    private UUID walletId;

    @NotNull(message = "operationType cannot be null")
    private OperationType operationType;

    @NotNull(message = "amount cannot be null")
    @Positive(message = "amount must be positive")
    private BigDecimal amount;

}