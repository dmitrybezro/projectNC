package com.bankNC.entity;

import com.bankNC.annotations.Attribute;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigInteger;


@AllArgsConstructor
@NoArgsConstructor
public class Task extends BaseEntity{

    @Attribute("9")
    private BigInteger idAccountSend;

    @Attribute("10")
    private BigInteger idAccountReceive;
    @Setter
    @Getter
    @Attribute("11")
    private String status;

    @Attribute("12")
    private Double transferAmount;
    @Setter
    @Getter
    @Attribute("14")
    private String errorMessage;

    public Task(BigInteger idAccountSend, BigInteger idAccountReceive, String status, Double transferAmount){
        this.idAccountSend = idAccountSend;
        this.idAccountReceive = idAccountReceive;
        this.status = status;
        this.transferAmount = transferAmount;
    }

    public Task(String status, String errorMessage){
        this.status = status;
        this.errorMessage = errorMessage;
    }

}
