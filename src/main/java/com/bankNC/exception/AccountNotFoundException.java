package com.bankNC.exception;

import com.bankNC.model.TransferRequestModel;
import lombok.Getter;

import java.math.BigInteger;

public class AccountNotFoundException extends Exception{
    private BigInteger idTransaction;
    public AccountNotFoundException(String message) {
        super(message);
    }
    public AccountNotFoundException(String message, BigInteger idTransaction){
        super(message);
        this.idTransaction = idTransaction;
    }
    public TransferRequestModel getTr(){
        return new TransferRequestModel(idTransaction, "Error", getMessage());
    }
}
