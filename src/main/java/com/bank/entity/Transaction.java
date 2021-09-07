package com.bank.entity;

import com.bank.annotations.Attribute;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Date;
@NoArgsConstructor
@AllArgsConstructor
@Setter
@Getter
public class Transaction extends BaseEntity{
    @Attribute("14")
    private String type;

    @Attribute("15")
    private Double payment;

    @Attribute("16")
    private Date dateTransaction;
}
