package com.bankNC.dto;

import lombok.*;

import javax.persistence.*;
import java.io.Serializable;
import java.math.BigInteger;

@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
@IdClass(ValueKey.class)
@Entity
@Table(name = "values")
public class ValueDto implements Serializable {
    @Id
    @Column(name = "obj_id")
    private BigInteger objectId;

    @Id
    @Column(name = "attr_id")
    private BigInteger attributeId;

    @Column(name = "param_val")
    private String parameterValue;
}

@AllArgsConstructor
@EqualsAndHashCode
@ToString
@NoArgsConstructor
class ValueKey implements Serializable {
    @Getter
    @Setter
    private BigInteger objectId;

    @Getter
    @Setter
    private BigInteger attributeId;
}