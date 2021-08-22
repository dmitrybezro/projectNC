package com.bankNC.entity;

import com.bankNC.annotations.Attribute;
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

    @Getter
    @Setter
    private List<Account> listAccount;

    @Setter
    @Getter
    private Map<String, Double> totalBalance;

    {
        listAccount = new ArrayList<>();
        totalBalance = new HashMap<>();
        totalBalance.put("RUB", 0.0);
        totalBalance.put("USD", 0.0);
        totalBalance.put("EUR", 0.0);
    }

    private Map<String, Double> upTotalBalance(){

        for(int i = 0; i < listAccount.size(); i++){
            switch (listAccount.get(i).getCurrency()){
                case "RUB":
                    totalBalance.put("RUB", totalBalance.get("RUB") + listAccount.get(i).getBalance());
                    break;
                case "USD":
                    totalBalance.put("USD", totalBalance.get("USD") + listAccount.get(i).getBalance());
                    break;
                case "EUR":
                    totalBalance.put("EUR", totalBalance.get("EUR") + listAccount.get(i).getBalance());
                    break;
            }
        }

        return this.totalBalance;
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
