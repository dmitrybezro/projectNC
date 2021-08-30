package com.bankNC.service;

import com.bankNC.converters.EntityDtoConverter;
import com.bankNC.dto.ObjectDto;
import com.bankNC.dto.ValueDto;
import com.bankNC.entity.Account;
import com.bankNC.entity.BaseEntity;
import com.bankNC.entity.Transaction;
import com.bankNC.exception.NegativeAccountBalanceException;
import com.bankNC.repository.ObjectsRepository;
import com.bankNC.repository.ValuesRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigInteger;
import java.util.List;

@Service
public class EntityService {

    @Autowired
    private ObjectsRepository objectsRepository;

    @Autowired
    private ValuesRepository valueRepository;

    public <T extends BaseEntity> T getById(BigInteger id, Class<T> clazz) throws IllegalAccessException, InstantiationException {
        ObjectDto objectDto = objectsRepository.findByObjectId(id);
        List<ValueDto> listValue = valueRepository.findAllByObjectId(id);

        T entity = EntityDtoConverter.toEntity(objectDto, listValue, clazz);

        return entity;
    }

    public <T extends BaseEntity> BigInteger saveEntity(T entity) throws IllegalAccessException, NegativeAccountBalanceException {
        ObjectDto objectDto1 = EntityDtoConverter.toDto(entity).getKey();
        objectsRepository.save(objectDto1);
        List<ValueDto> listValues = EntityDtoConverter.toDto(entity).getValue();
        for(ValueDto value : listValues){
            value.setObjectId(objectDto1.getObjectId());
        }
        valueRepository.saveAll(listValues);
        return objectDto1.getObjectId();
    }

    public <T extends BaseEntity> void updateEntity(T entity) throws IllegalAccessException {
        List<ValueDto> listValues = EntityDtoConverter.toDto(entity).getValue();
        valueRepository.saveAll(listValues);
    }

    public <T extends BaseEntity> T getByParentId(BigInteger parentId, Class<T> clazz) throws IllegalAccessException, InstantiationException, NegativeAccountBalanceException {
        ObjectDto objectDto2 = objectsRepository.findByParentIdAndObjectType(parentId, 1);
        List<ValueDto> listValue = valueRepository.findAllByObjectId(objectDto2.getObjectId());
        T entity = EntityDtoConverter.toEntity(objectDto2, listValue, clazz);
        return entity;
    }

    public void saveTransaction (Transaction transaction, BigInteger parentId) throws IllegalAccessException {
        ObjectDto objectDto = EntityDtoConverter.toDto(transaction).getKey();
        objectDto.setParentId(parentId);
        objectDto.setObjectName("transaction");
        objectsRepository.save(objectDto);
        List<ValueDto> listValues = EntityDtoConverter.toDto(transaction).getValue();
        for(ValueDto value : listValues){
            value.setObjectId(objectDto.getObjectId());
        }
        valueRepository.saveAll(listValues);
    }
}

