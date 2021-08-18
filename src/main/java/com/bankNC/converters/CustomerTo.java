package com.bankNC.converters;

import com.bankNC.entity.Customer;
import com.bankNC.entity.forTables.ObjectDto;
import com.bankNC.entity.forTables.ValueDto;
import com.bankNC.repository.ObjectsRepository;
import org.springframework.beans.factory.annotation.Autowired;

import java.sql.Date;
import java.util.ArrayList;
import java.util.List;

public class CustomerTo {

    public static ObjectDto splitObject(Customer customer) {
        ObjectDto objectDto = new ObjectDto();

        objectDto.setParId(null);
        objectDto.setObjType(1);
        objectDto.setObjName(null);
        objectDto.setObjDoc(new Date(10000));

        return objectDto;
    }

    public static List<ValueDto> splitValues(Customer customer, Integer objectId){
        List<ValueDto> listValues = new ArrayList<ValueDto>(4);

        for(int i = 0; i < 4; i++){
            listValues.add(new ValueDto());
            listValues.get(i).setObjId(objectId);
        }

        listValues.get(0).setAttrId(1);
        listValues.get(0).setParam_val(customer.getFirstName());

        listValues.get(1).setAttrId(2);
        listValues.get(1).setParam_val(customer.getLastName());

        listValues.get(2).setAttrId(3);
        listValues.get(2).setParam_val(customer.getPatronymic());

        listValues.get(3).setAttrId(4);
        listValues.get(3).setParam_val(customer.getDatOfBirth());




        return listValues;
    }

}
