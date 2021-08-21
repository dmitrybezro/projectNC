package com.bankNC.entity;

import com.bankNC.annotations.Attribute;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.boot.context.properties.ConstructorBinding;

import java.sql.Date;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;


@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class Customer extends BaseEntity{
    @Attribute("1")
    private String firstName;

    @Attribute("2")
    private String lastName;

    @Attribute("3")
    private String patronymic;

    @Attribute("4")
    private String datOfBirth;

    @Attribute("5")
    private Integer numberAccount;

    private List<Account> listAccount;
    {
        listAccount = new ArrayList<>();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Customer)) return false;
        if (!super.equals(o)) return false;
        Customer customer = (Customer) o;
        return Objects.equals(firstName, customer.firstName) &&
                Objects.equals(lastName, customer.lastName) &&
                 Objects.equals(patronymic, customer.patronymic) &&
                  Objects.equals(datOfBirth, customer.datOfBirth) &&
                   Objects.equals(numberAccount, customer.numberAccount);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), firstName, lastName, patronymic, datOfBirth, numberAccount);
    }
}
