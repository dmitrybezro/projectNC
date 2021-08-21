package com.bankNC.entity;

import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Setter
@Getter

public class Account {
    private Double balance;
    private String currency;
    private String dataOpen;
    private Integer closeOperation;
    private List<Operation> listOperation;

    {
        listOperation = new ArrayList<>();
    }

    public Account(){}
}
