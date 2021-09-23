package com.bank.entity;

import com.bank.annotations.Attribute;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import java.math.BigInteger;
import java.util.Date;

import static com.bank.constants.Attributes.*;

@AllArgsConstructor
@NoArgsConstructor
public class Task extends BaseEntity{
    @Setter
    @Attribute(ID_ACCOUNT_SEND)
    private BigInteger idAccountSend;

    @Setter
    @Attribute(ID_ACCOUNT_RECV)
    private BigInteger idAccountReceive;

    @Setter
    @Getter
    @Attribute(STATUS)
    private String status;

    @Setter
    @Attribute(TRANSFER_AMOUNT)
    private Double transferAmount;

    @Setter
    @Getter
    @Attribute(ERROR_MESSAGE)
    private String errorMessage;

    public Task(BigInteger idAccountSend, BigInteger idAccountReceive, String status, Double transferAmount){
        this.idAccountSend = idAccountSend;
        this.idAccountReceive = idAccountReceive;
        this.status = status;
        this.transferAmount = transferAmount;
        this.setCreationDate(new java.sql.Date(System.currentTimeMillis()));
        this.setParentId(idAccountSend);
        this.setName("task "+idAccountSend+" " +new Date());
    }

//    public Task(String status, String errorMessage){
//        this.status = status;
//        this.errorMessage = errorMessage;
//        this.setCreationDate(new java.sql.Date(System.currentTimeMillis()));
//        this.setParentId(idAccountSend);
//        this.setName("task "+idAccountSend+" " +new Date());
//    }
}
