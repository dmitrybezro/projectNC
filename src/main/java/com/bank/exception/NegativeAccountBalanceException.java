package com.bank.exception;

public class NegativeAccountBalanceException extends Exception{
    public NegativeAccountBalanceException(String message) {
        super(message);
    }
}
