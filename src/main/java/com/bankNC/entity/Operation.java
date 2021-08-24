package com.bankNC.entity;

import com.bankNC.annotations.Attribute;
import lombok.*;

import java.math.BigInteger;
import java.util.Date;

@Setter@Getter
@AllArgsConstructor
@NoArgsConstructor
public class Operation extends BaseEntity {
    @Attribute("9")
    private BigInteger idAccountSend;
    @Attribute("10")
    private BigInteger idAccountReceive;
    @Attribute("11")
    private String status;
    @Attribute("12")
    private Double transferAmount;
    @Attribute("13")
    private Date dataOperation;
    @Attribute("14")
    private String message;
}
