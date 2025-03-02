
package com.example.controller;

import com.example.AbstractTest;
import com.example.dto.request.TransactionRequest;
import com.example.dto.response.TransactionResponse;
import com.example.dto.response.WalletResponse;
import com.example.enums.OperationType;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MvcResult;

import java.math.BigDecimal;
import java.util.UUID;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class WalletControllerTest extends AbstractTest {

    @Nested
    @DisplayName("Transaction Creation")
    class TransactionCreationTests {

        @Test
        @DisplayName("Should create deposit transaction and return 200")
        void shouldCreateDepositTransactionAndReturn200() throws Exception {
            OperationType operationType = OperationType.DEPOSIT;

            TransactionRequest request = TransactionRequest.builder()
                    .walletId(WALLET_UUID_NOT_FOUND)
                    .operationType(operationType)
                    .amount(DEPOSIT_AMOUNT)
                    .build();

            String requestBody = objectMapper.writeValueAsString(request);

            MvcResult result = mockMvc.perform(post("/api/v1/wallet")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(requestBody))
                    .andExpect(status().isOk())
                    .andReturn();

            String responseBody = result.getResponse().getContentAsString();
            TransactionResponse actualResponse = objectMapper.readValue(responseBody, TransactionResponse.class);

            assertAll("Transaction Response Assertions",
                    () -> assertEquals(WALLET_UUID_NOT_FOUND, actualResponse.getWalletId()),
                    () -> assertEquals(DEPOSIT_AMOUNT, actualResponse.getAmount()),
                    () -> assertEquals(operationType, actualResponse.getOperationType()),
                    () -> assertNotNull(actualResponse.getTransactionId())
            );
        }

        @Test
        @DisplayName("Should create withdraw transaction and return 200")
        void shouldCreateWithdrawTransactionAndReturn200() throws Exception {
            UUID walletId = WALLET_UUID_EXISTS;
            OperationType operationType = OperationType.WITHDRAW;

            TransactionRequest request = TransactionRequest.builder()
                    .walletId(walletId)
                    .operationType(operationType)
                    .amount(WITHDRAW_AMOUNT)
                    .build();

            String requestBody = objectMapper.writeValueAsString(request);

            MvcResult result = mockMvc.perform(post("/api/v1/wallet")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(requestBody))
                    .andExpect(status().isOk())
                    .andReturn();

            String responseBody = result.getResponse().getContentAsString();
            TransactionResponse actualResponse = objectMapper.readValue(responseBody, TransactionResponse.class);

            assertAll("Transaction Response Assertions",
                    () -> assertEquals(walletId, actualResponse.getWalletId()),
                    () -> assertEquals(WITHDRAW_AMOUNT, actualResponse.getAmount()),
                    () -> assertEquals(operationType, actualResponse.getOperationType()),
                    () -> assertNotNull(actualResponse.getTransactionId())
            );

            WalletResponse actualWalletResponse = getWalletResponse(walletId);
            BigDecimal expectedAmount = DEPOSIT_AMOUNT.subtract(WITHDRAW_AMOUNT);

            assertAll("Wallet Response Assertions After Withdrawal",
                    () -> assertEquals(walletId, actualWalletResponse.getWalletId()),
                    () -> assertEquals(expectedAmount, actualWalletResponse.getAmount())
            );
        }

        @Test
        @DisplayName("Should fail withdraw transaction due to insufficient funds and return 402")
        void shouldFailWithdrawTransactionDueToInsufficientFundsAndReturn402() throws Exception {
            OperationType operationType = OperationType.WITHDRAW;

            TransactionRequest request = TransactionRequest.builder()
                    .walletId(WALLET_UUID_EXISTS)
                    .operationType(operationType)
                    .amount(WITHDRAW_NOT_ENOUGH_AMOUNT)
                    .build();

            String requestBody = objectMapper.writeValueAsString(request);

            mockMvc.perform(post("/api/v1/wallet")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(requestBody))
                    .andExpect(status().isPaymentRequired());

            WalletResponse actualWalletResponse = getWalletResponse(WALLET_UUID_EXISTS);

            assertAll("Wallet Response Assertions After Failed Withdrawal",
                    () -> assertEquals(WALLET_UUID_EXISTS, actualWalletResponse.getWalletId()),
                    () -> assertEquals(DEPOSIT_AMOUNT, actualWalletResponse.getAmount())
            );
        }
    }

    @Nested
    @DisplayName("Wallet Retrieval")
    class WalletRetrievalTests {

        @Test
        @DisplayName("Should get wallet by UUID and return 200")
        void shouldGetWalletByUuidAndReturn200() throws Exception {
            WalletResponse actualWalletResponse = getWalletResponse(WALLET_UUID_EXISTS);

            assertAll("Wallet Response Assertions",
                    () -> assertEquals(WALLET_UUID_EXISTS, actualWalletResponse.getWalletId()),
                    () -> assertEquals(DEPOSIT_AMOUNT, actualWalletResponse.getAmount())
            );
        }

        @Test
        @DisplayName("Should return 404 when wallet not found")
        void shouldReturn404WhenWalletNotFound() throws Exception {
            mockMvc.perform(get("/api/v1/wallet/" + WALLET_UUID_NOT_FOUND)
                            .contentType(MediaType.APPLICATION_JSON))
                    .andExpect(status().isNotFound());
        }
    }

    private WalletResponse getWalletResponse(UUID walletId) throws Exception {
        MvcResult getWalletResult = mockMvc.perform(get("/api/v1/wallet/" + walletId)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn();

        String walletResponseBody = getWalletResult.getResponse().getContentAsString();
        return objectMapper.readValue(walletResponseBody, WalletResponse.class);
    }

}
