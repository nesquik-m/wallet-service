package com.example.controller;

import com.example.dto.response.WalletResponse;
import com.example.mapper.TransactionMapper;
import com.example.dto.request.WalletTransactionRequest;
import com.example.mapper.WalletMapper;
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

    private final TransactionService transactionService;

    private final WalletService walletService;

    private final TransactionMapper transactionMapper;

    private final WalletMapper walletMapper;

    @PostMapping
    public String processOperation(@Valid @RequestBody WalletTransactionRequest request) {
        transactionService.createTransaction(transactionMapper.mapToTransaction(request));
        return "success";
    }

    @GetMapping("/{WALLET_UUID}")
    public WalletResponse getWallet(@PathVariable(name = "WALLET_UUID") UUID walletId) {
        return walletMapper.mapToWalletResponse(walletService.getWallet(walletId));
    }

}