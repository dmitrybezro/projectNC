package com.bank.entity;

import lombok.Getter;
import lombok.Setter;
import java.math.BigInteger;
import java.sql.Date;
import java.util.Objects;

@Getter
@Setter
public class BaseEntity {
    protected BigInteger id;
    protected BigInteger parentId;
    protected String name;
    protected Date creationDate;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        BaseEntity that = (BaseEntity) o;
        return id.equals(that.id) &&
                name.equals(that.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name);
    }
}
