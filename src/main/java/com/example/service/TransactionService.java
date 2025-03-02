package com.example.service;

import com.example.dto.response.TransactionResponse;
import com.example.model.Transaction;

public interface TransactionService {

    TransactionResponse createTransaction(Transaction transaction);

}