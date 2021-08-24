package com.bankNC.exception;

import com.bankNC.model.TransferRequestModel;
import lombok.Getter;

import java.math.BigInteger;

public class NegativeAccountBalanceException extends Exception{
    private BigInteger idTransaction;
    public NegativeAccountBalanceException(String message) {
        super(message);
    }
    public NegativeAccountBalanceException(String message, BigInteger idTransaction){
        super(message);
        this.idTransaction = idTransaction;
    }

    public TransferRequestModel getTr(){
        return new TransferRequestModel(idTransaction, "Error", getMessage());
    }
}
