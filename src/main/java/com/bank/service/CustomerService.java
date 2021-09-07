package com.bank.service;

import com.bank.converters.EntityDtoConverter;
import com.bank.dto.ObjectDto;
import com.bank.entity.Customer;
import com.bank.dto.ValueDto;
import com.bank.exception.CustomerNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.bank.repository.ObjectsRepository;
import com.bank.repository.ValuesRepository;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;

@Service
public class CustomerService {
    @Autowired
    private ObjectsRepository objectsRepository;

    @Autowired
    private ValuesRepository valueRepository;

    @Autowired
    private EntityService entityService;

    public Customer getOne(BigInteger objectId) throws CustomerNotFoundException, IllegalAccessException, InstantiationException {
        return entityService.getById(objectId, Customer.class);
    }
}
