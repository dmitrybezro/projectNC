package com.bankNC.model;

import lombok.AllArgsConstructor;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigInteger;


@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class TransferRequestModel {
    private BigInteger idTransaction;
    private String status;
    private String errorMessage;

    public TransferRequestModel(BigInteger idTransaction, String status){
        this.idTransaction = idTransaction;
        this.status = status;
    }
}
