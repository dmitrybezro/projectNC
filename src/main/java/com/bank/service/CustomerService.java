package com.bank.service;

/*import com.bankNC.converters.CustomerTo;*/
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

    public void AddCustomer(Customer customer) throws IllegalAccessException {
        objectsRepository.save(EntityDtoConverter.toDto(customer).getKey());
        valueRepository.saveAll(EntityDtoConverter.toDto(customer).getValue());
    }

    public Customer getOne(BigInteger objectId) throws CustomerNotFoundException, IllegalAccessException, InstantiationException {

        ObjectDto objectDto = objectsRepository.findByObjectId(objectId);
        List<ValueDto> listAttr = new ArrayList<>();

        listAttr = valueRepository.findAllByObjectId(objectId);

        if(listAttr.size() == 0){
            throw new CustomerNotFoundException("Пользователь не найден");
        }
        Customer customer = new Customer();
        customer = EntityDtoConverter.toEntity(objectDto, listAttr, Customer.class);
        customer.setName("Customer");

        return customer;
    }
}
