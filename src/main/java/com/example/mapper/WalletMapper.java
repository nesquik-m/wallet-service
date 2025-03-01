package com.example.mapper;

import com.example.dto.response.WalletResponse;
import com.example.model.Wallet;
import org.springframework.stereotype.Component;

@Component
public class WalletMapper {

    public WalletResponse mapToWalletResponse(Wallet wallet) {
        return WalletResponse.builder()
                .walletId(wallet.getId())
                .amount(wallet.getBalance())
                .build();
    }

}