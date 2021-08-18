package com.bankNC.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConstructorBinding;

import java.sql.Date;
import java.util.ArrayList;
import java.util.List;


@AllArgsConstructor
@Getter
@Setter
public class Customer {
    private String firstName;
    private String lastName;
    private String patronymic;
    private String datOfBirth;
    private int numberAccount;
    private List<Account> listAccount;
    {
        listAccount = new ArrayList<>();
    }

    public Customer(){
    }
}
