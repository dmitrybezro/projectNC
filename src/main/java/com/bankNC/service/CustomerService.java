package com.bankNC.service;

import com.bankNC.converters.CustomerTo;
import com.bankNC.entity.Customer;
import com.bankNC.entity.forTables.ObjectDto;
import com.bankNC.entity.forTables.ValueDto;
import com.bankNC.exception.CustomerNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.bankNC.repository.ObjectsRepository;
import com.bankNC.repository.ValueRepository;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;


@Service
public class CustomerService {
    @Autowired
    private ObjectsRepository objectsRepository;

    @Autowired
    private ValueRepository valueRepository;

    public void AddCustomer(Customer customer){
        ObjectDto objectDto = CustomerTo.splitObject(customer);
        List<ValueDto> listValue = CustomerTo.splitValues(customer, 1010+
                (int)objectsRepository.count());

        objectsRepository.save(objectDto);
        for(int i = 0 ; i < 4; i++){
            valueRepository.save(listValue.get(i));
        }
    }

    public Customer getOne(Integer objId) throws CustomerNotFoundException{
        //ObjectDto objectDto = objectsRepository.findById(objId).get();

        Customer customer = new Customer();



     //   customer.setFirstName(valueRepository.findByAttrIdAndObjId( 1, objId    ).getParam_val());
       // customer.setFirstName(valueRepository.findByAttrIdAndObjId( 2, objId    ).getParam_val());
       // customer.setPatronymic(valueRepository.findByObjIdAndAttrId(objId, 3).getParam_val());
     //   customer.setDatOfBirth(valueRepository.findByObjIdAndAttrId(objId, 4).getParam_val());
      //  customer.setNumberAccount(Integer.parseInt(valueRepository.findByObjIdAndAttrId(objId, 5).getParam_val()));

        List<ValueDto> listAttr = valueRepository.findByObjId(objId);
        if(listAttr.size() == 0){
            throw new CustomerNotFoundException("Пользователь не найден");
        }
        String param;
        for(int i = 0; i < listAttr.size(); i++){
            param = listAttr.get(i).getParam_val();

            switch(listAttr.get(i).getAttrId()){
                case 1:
                    customer.setFirstName(param);
                    break;
                case 2:
                    customer.setLastName(param);
                    break;
                case 3:
                    customer.setPatronymic(param);
                    break;
                case 4:
                    customer.setDatOfBirth(param);
                    break;
                case 5:
                    customer.setNumberAccount(Integer.parseInt(param));
                    break;
            }
        }

        return customer;
    }
}
