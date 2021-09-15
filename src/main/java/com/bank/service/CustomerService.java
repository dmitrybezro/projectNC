package com.bank.service;

import com.bank.dto.ValueDto;
import com.bank.entity.Customer;
import com.bank.exception.CustomerNotFoundException;
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

    public Customer getOne(BigInteger objectId) throws CustomerNotFoundException, IllegalAccessException, InstantiationException {
        return entityService.getById(objectId, Customer.class);
    }

    public Customer getByUserName(String username) throws IllegalAccessException, InstantiationException, CustomerNotFoundException {
        ValueDto value = valueRepository.getByParameterValue(username);
        if(value == null) {
            return null;
        }

        return getOne(value.getObjectId());
    }
}
