package com.bank.repository;

import com.bank.dto.ValueDto;
import org.springframework.data.repository.CrudRepository;

import java.math.BigInteger;
import java.util.List;

public interface ValuesRepository  extends CrudRepository<ValueDto, Integer> {
    List<ValueDto> findAllByObjectId(BigInteger objectId);
    ValueDto getByParameterValue(String value);
}
