package com.bankNC.service;

import com.bankNC.converters.EntityDtoConverter;
import com.bankNC.dto.ObjectDto;
import com.bankNC.dto.ValueDto;
import com.bankNC.entity.Account;
import com.bankNC.entity.BaseEntity;
import com.bankNC.entity.Transaction;
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

        T entity = EntityDtoConverter.toEntity(objectDto, listValue,clazz);

        return entity;
    }

    public <T extends BaseEntity> BigInteger saveEntity(T entity) throws IllegalAccessException {
        ObjectDto objectDto = EntityDtoConverter.toDto(entity).getKey();
        objectsRepository.save(objectDto);
        valueRepository.saveAll(EntityDtoConverter.toDto(entity).getValue());
        return objectDto.getObjectId();
    }

    public <T extends BaseEntity> T getByParentId(BigInteger parentId, Class<T> clazz) throws IllegalAccessException, InstantiationException {
        ObjectDto objectDto = objectsRepository.findByParentIdAndObjectType(parentId, 1);
        List<ValueDto> listValue = valueRepository.findAllByObjectId(parentId);
        T entity = EntityDtoConverter.toEntity(objectDto, listValue, clazz);
        return entity;
    }

    public void saveTransaction(Transaction transaction, BigInteger parentId) throws IllegalAccessException {
        ObjectDto objectDto = EntityDtoConverter.toDto(transaction).getKey();
        objectDto.setParentId(parentId);
        objectsRepository.save(objectDto);
        valueRepository.saveAll(EntityDtoConverter.toDto(transaction).getValue());
    }
}
