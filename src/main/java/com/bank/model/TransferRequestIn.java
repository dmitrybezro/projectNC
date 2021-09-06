package com.bank.model;

import lombok.AllArgsConstructor;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigInteger;


@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class TransferRequestIn {
    private BigInteger idAccountSend;
    private BigInteger idAccountReceive;
    private Double sum;
}
