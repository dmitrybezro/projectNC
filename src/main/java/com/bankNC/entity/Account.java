package com.bankNC.entity;

import com.bankNC.annotations.Attribute;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
public class Account extends BaseEntity{
    @Setter@Getter
    @Attribute("6")
    private Double balance;

    @Getter@Setter
    @Attribute("7")
    private String currency;

    @Getter@Setter
    @Attribute("8")
    private String dataOpen;

    private List<Operation> listOperation;

    {
        listOperation = new ArrayList<>();
    }

    public Account(Account account){

        this.balance = account.balance;
        this.currency = account.currency;
        this.dataOpen = account.dataOpen;
    }
}
