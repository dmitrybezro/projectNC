package com.bank.service;

import com.bank.dto.ValueDto;
import com.bank.entity.Customer;
import com.bank.repository.ValuesRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigInteger;

@Service
public class CustomerService {
    @Autowired
    private EntityService entityService;

    @Autowired
    private ValuesRepository valueRepository;

    public Customer getOneCustomer(BigInteger objectId) throws IllegalAccessException, InstantiationException {
        return entityService.getById(objectId, Customer.class);
    }

    public Customer getByUserName(String username) throws IllegalAccessException, InstantiationException {
        ValueDto value = valueRepository.getByParameterValue(username);
        if(value == null) {
            return null;
        }

        return getOneCustomer(value.getObjectId());
    }
}
