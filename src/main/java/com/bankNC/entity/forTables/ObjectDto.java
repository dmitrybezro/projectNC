package com.bankNC.entity.forTables;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

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
    private Integer objId;

    @Column(name = "par_id")
    private Integer parId;

    @Column(name = "obj_type")
    private Integer objType;

    @Column(name = "obj_name")
    private String objName;


    @Column(name = "obj_doc")
    private Date objDoc;

    public ObjectDto(){
    }
}
