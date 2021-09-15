package com.bank.entity;

import com.bank.annotations.Attribute;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.*;

@AllArgsConstructor
@NoArgsConstructor
public class Customer extends BaseEntity{
    @Getter
    @Setter
    @Attribute("1")
    private String firstName;

    @Getter
    @Setter
    @Attribute("2")
    private String lastName;

    @Getter
    @Setter
    @Attribute("3")
    private String patronymic;

    @Getter
    @Setter
    @Attribute("4")
    private String datOfBirth;

    @Getter
    @Setter
    @Attribute("5")
    private Integer numberAccount;

    @Setter
    @Getter
    @Attribute("100")
    private String login;

    @Setter
    @Getter
    @Attribute("101")
    private String passwordHash;

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
