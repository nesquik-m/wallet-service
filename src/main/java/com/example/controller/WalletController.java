package com.example.controller;

import com.example.WalletMapper;
import com.example.dto.request.WalletTransactionRequest;
import com.example.service.WalletService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/wallet")
@RequiredArgsConstructor
public class WalletController {

    private final WalletService walletService;

    private final WalletMapper walletMapper;

    @PostMapping
    public void processOperation(@RequestBody WalletTransactionRequest request) {

    }

}