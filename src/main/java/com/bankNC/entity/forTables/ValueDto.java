package com.bankNC.entity.forTables;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

@AllArgsConstructor
@Setter
@Getter
@Entity
@Table(name = "values")
public class ValueDto {
    @Id
    @Column(name = "obj_id")
    private Integer objId;

    @Column(name = "attr_id")
    private Integer attrId;

    @Column(name = "param_val")
    private String param_val;
    public ValueDto(){
    }
}
