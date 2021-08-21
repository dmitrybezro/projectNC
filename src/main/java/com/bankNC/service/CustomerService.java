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

/*    public void AddCustomer(Customer customer){
        ObjectDto objectDto = CustomerTo.splitObject(customer);
        List<ValueDto> listValue = CustomerTo.splitValues(customer, 1010+
                (int)objectsRepository.count());

        objectsRepository.save(objectDto);
        for(int i = 0 ; i < 4; i++){
            valueRepository.save(listValue.get(i));
        }
    }*/

    public Customer getOne(BigInteger objectId) throws CustomerNotFoundException, IllegalAccessException, InstantiationException {

        ObjectDto objectDto = objectsRepository.findByObjectId(objectId);
        List<ValueDto> listAttr = new ArrayList<>();

        listAttr = valueRepository.findAllByObjectId(objectId);

        if(listAttr.size() == 0){
            throw new CustomerNotFoundException("Пользователь не найден");
        }

        //  Нужно собрать кастомера
       // EntityDtoConverter.Pair<ObjectDto, List<ValueDto>> dtos = new EntityDtoConverter.Pair<>(objectDto, listAttr);
        Customer customer = EntityDtoConverter.toEntity(new EntityDtoConverter.Pair<ObjectDto, List<ValueDto>>(objectDto, listAttr), Customer.class);

        return customer;
    }

}