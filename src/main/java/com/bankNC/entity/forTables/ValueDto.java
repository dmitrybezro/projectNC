package com.bankNC.entity.forTables;

import lombok.*;
import org.hibernate.Interceptor;

import javax.persistence.*;
import java.io.Serializable;

@AllArgsConstructor
//@NoArgsConstructor
@Setter
@Getter
@IdClass(ValueDto.ValueKey.class)
@Entity
@Table(name = "values")
public class ValueDto implements Serializable {
    @Id
    @Column(name = "obj_id")
    private Integer objid;

    @Id
    @Column(name = "attr_id")
    private Integer attrId;

    @Column(name = "param_val")
    private String param_val;

    public ValueDto(){
    }

    @AllArgsConstructor
    @EqualsAndHashCode
    @ToString
    @NoArgsConstructor
  //  @Embeddable
    public class ValueKey implements Serializable {
        @Getter
        @Setter
        private Integer objid;

        @Getter
        @Setter
        private Integer attrId;

    }
}
