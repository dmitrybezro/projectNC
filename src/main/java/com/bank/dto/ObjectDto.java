package com.bank.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import javax.persistence.*;
import java.math.BigInteger;
import java.sql.Date;

@Getter
@Setter
@AllArgsConstructor
@Entity
@Table(name = "objects")
public class ObjectDto {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "seq_obj")
    @SequenceGenerator(name = "seq_obj", sequenceName = "seq_obj", allocationSize = 1)
    @Column(name = "obj_id")
    private BigInteger objectId;

    @Column(name = "obj_type")
    private String objectType;

    @Column(name = "par_id")
    private BigInteger parentId;

    @Column(name = "obj_name")
    private String objectName;

    @Column(name = "obj_doc")
    private Date objectDoc;

    public ObjectDto(BigInteger id, String name){
        this.objectId = id;
        this.objectName = name;
    }

    public ObjectDto(ObjectDto objectDto){
        this.parentId = objectDto.parentId;
        this.objectName = objectDto.objectName;
        this.objectDoc = objectDto.objectDoc;
    }

    public ObjectDto(){
    }
}
