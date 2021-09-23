package com.bank.entity;

import com.bank.annotations.Attribute;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigInteger;
import java.util.Date;

import static com.bank.constants.Attributes.*;

@NoArgsConstructor
@Setter
@Getter
public class Transaction extends BaseEntity{
    @Attribute(TYPE)
    private String type;

    @Attribute(PAYMENT)
    private Double payment;

    @Attribute(TRANSACTION_DATE)
    private Date dateTransaction;

    @Attribute(ID_ACCOUNT_SEND_OR_RECV)
    private BigInteger idAccSendOrRecv;

   public Transaction(String type, Double payment, BigInteger idAccSendOrRecv){
        this.type = type;
        this.payment = payment;
        this.idAccSendOrRecv = idAccSendOrRecv;
        this.setObjectType("transaction");
        this.name = String.format("transaction_%s_%s", idAccSendOrRecv, new Date().getTime());
        this.setCreationDate(new java.sql.Date(System.currentTimeMillis()));
        this.setDateTransaction(new java.sql.Date(System.currentTimeMillis()));
    }
}
