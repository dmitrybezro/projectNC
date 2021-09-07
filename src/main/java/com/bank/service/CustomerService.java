package com.bank.service;

import com.bank.entity.Customer;
import com.bank.exception.CustomerNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigInteger;

@Service
public class CustomerService {
    @Autowired
    private EntityService entityService;

    public Customer getOne(BigInteger objectId) throws CustomerNotFoundException, IllegalAccessException, InstantiationException {
        return entityService.getById(objectId, Customer.class);
    }
}
