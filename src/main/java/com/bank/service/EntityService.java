package com.bank.service;

import com.bank.converters.EntityDtoConverter;
import com.bank.dto.ObjectDto;
import com.bank.dto.ValueDto;
import com.bank.entity.BaseEntity;
import com.bank.entity.Transaction;
import com.bank.repository.ObjectsRepository;
import com.bank.repository.ValuesRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigInteger;
import java.sql.Date;
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

    public <T extends BaseEntity> T getByIdAndParentId(BigInteger id, BigInteger parentId,Class<T> clazz) throws IllegalAccessException, InstantiationException {
        ObjectDto objectDto = objectsRepository.findByObjectIdAndParentId(id, parentId);
        if(objectDto==null)
            return null;
        List<ValueDto> listValue = valueRepository.findAllByObjectId(id);

        T entity = EntityDtoConverter.toEntity(objectDto, listValue, clazz);

        return entity;
    }

    @Transactional
    public <T extends BaseEntity> BigInteger saveEntity(T entity) throws IllegalAccessException {
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

    public <T extends BaseEntity> T getByParentId(BigInteger parentId, Class<T> clazz) throws IllegalAccessException, InstantiationException {
        ObjectDto objectDto2 = objectsRepository.findByParentIdAndObjectType(parentId, "account");
        List<ValueDto> listValue = valueRepository.findAllByObjectId(objectDto2.getObjectId());
        T entity = EntityDtoConverter.toEntity(objectDto2, listValue, clazz);
        return entity;
    }

    public void saveTransaction (Transaction transaction, BigInteger parentId) throws IllegalAccessException {
        ObjectDto objectDto = EntityDtoConverter.toDto(transaction).getKey();
        objectDto.setParentId(parentId);
        objectDto.setObjectName("transaction");
        objectDto.setObjectDoc(new Date(transaction.getDateTransaction().getTime()));
        objectsRepository.save(objectDto);
        List<ValueDto> listValues = EntityDtoConverter.toDto(transaction).getValue();
        for(ValueDto value : listValues){
            value.setObjectId(objectDto.getObjectId());
        }
        valueRepository.saveAll(listValues);
    }
}


