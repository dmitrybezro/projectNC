package com.bank.entity;

import com.bank.annotations.Attribute;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import java.util.Date;

import static com.bank.constants.Attributes.*;

@NoArgsConstructor
@AllArgsConstructor
@Setter
@Getter
public class Transaction extends BaseEntity{
    @Attribute(TYPE)
    private String type;

    @Attribute(PAYMENT)
    private Double payment;

    @Attribute(TRANSACTION_DATE)
    private Date dateTransaction;

    //  от кого и ид счете
}
