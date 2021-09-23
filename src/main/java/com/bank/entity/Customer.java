package com.bank.entity;

import com.bank.annotations.Attribute;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import java.util.*;

import static com.bank.constants.Attributes.*;

@AllArgsConstructor
@NoArgsConstructor
public class Customer extends BaseEntity{
    @Getter
    @Setter
    @Attribute(FIRST_NAME)
    private String firstName;

    @Getter
    @Setter
    @Attribute(LAST_NAME)
    private String lastName;

    @Getter
    @Setter
    @Attribute(PATRONYMIC)
    private String patronymic;

    @Getter
    @Setter
    @Attribute(BIRTH_DATE)
    private String datOfBirth;

    @Getter
    @Setter
    @Attribute(ACCOUNT_NUMBER)
    private Integer numberAccount;

    @JsonIgnore
    @Setter
    @Getter
    @Attribute(LOGIN)
    private String login;

    @JsonIgnore
    @Setter
    @Getter
    @Attribute(PASSWORD)
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
