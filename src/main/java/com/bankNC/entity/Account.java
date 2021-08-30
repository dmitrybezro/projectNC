package com.bankNC.entity;

import com.bankNC.annotations.Attribute;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

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

    public Account(Account account){

        this.balance = account.balance;
        this.currency = account.currency;
        this.dataOpen = account.dataOpen;
    }
}
