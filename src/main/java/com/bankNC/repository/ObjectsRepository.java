package com.bankNC.repository;

import com.bankNC.dto.ObjectDto;
import org.springframework.data.repository.CrudRepository;

import java.math.BigInteger;
import java.util.List;

public interface ObjectsRepository extends CrudRepository<ObjectDto, BigInteger> {
    ObjectDto findByObjectId(BigInteger objectId);
    List<ObjectDto> findByParentId(BigInteger parentId);
}
