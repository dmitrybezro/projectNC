package com.bankNC.service;

/*import com.bankNC.converters.CustomerTo;*/
import com.bankNC.converters.EntityDtoConverter;
import com.bankNC.dto.ObjectDto;
import com.bankNC.entity.Customer;
import com.bankNC.dto.ValueDto;
import com.bankNC.exception.CustomerNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.bankNC.repository.ObjectsRepository;
import com.bankNC.repository.ValuesRepository;

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
        customer = EntityDtoConverter.toEntity(new EntityDtoConverter.Pair<ObjectDto, List<ValueDto>>(objectDto, listAttr), Customer.class);
        customer.setName("Customer");

        return customer;
    }
}
