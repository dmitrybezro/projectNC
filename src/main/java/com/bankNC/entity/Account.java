package com.bankNC.entity;

import com.bankNC.annotations.Attribute;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;


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


    private Integer closeOperation;
    private List<Operation> listOperation;

    {
        listOperation = new ArrayList<>();
    }

    public Account(){}
}
