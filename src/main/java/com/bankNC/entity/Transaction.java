package com.bankNC.entity;

import com.bankNC.annotations.Attribute;
import lombok.AllArgsConstructor;

import java.util.Date;
@AllArgsConstructor
public class Transaction extends BaseEntity{
    private String type;
    @Attribute("12")
    private Double transferAmount;
    @Attribute("13")
    private Date dateTransaction;
}
