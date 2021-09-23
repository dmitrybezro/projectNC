package com.bank.entity;

import com.bank.annotations.Attribute;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import static com.bank.constants.Attributes.*;

@AllArgsConstructor
@NoArgsConstructor
public class Account extends BaseEntity{
    @Setter@Getter
    @Attribute(BALANCE)
    private Double balance;

    @Getter@Setter
    @Attribute(CURRENCY)
    private String currency;

    @JsonIgnore
    @Getter@Setter
    @Attribute(OPEN_DATE)
    private String dataOpen;

    public Account(Account account){

        this.balance = account.balance;
        this.currency = account.currency;
        this.dataOpen = account.dataOpen;
    }
}
