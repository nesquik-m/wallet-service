package com.example.exception;

import lombok.Getter;

@Getter
public class NotEnoughBalanceException extends RuntimeException {

    public NotEnoughBalanceException(String message) {
        super(message);
    }

}