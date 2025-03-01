package com.example.controller;

import com.example.mapper.TransactionMapper;
import com.example.dto.request.WalletTransactionRequest;
import com.example.service.TransactionService;
import com.example.service.WalletService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/api/v1/wallet")
@RequiredArgsConstructor
public class WalletController {

    private final WalletService walletService;

    private final TransactionService transactionService;

    private final TransactionMapper transactionMapper;

    @PostMapping
    public String processOperation(@Valid @RequestBody WalletTransactionRequest request) {
        transactionService.createTransaction(transactionMapper.mapToTransaction(request));
        return "success";
    }

    @GetMapping("/{WALLET_UUID}")
    public void getWallet(@PathVariable(name = "WALLET_UUID") UUID walletId) {

    }

}