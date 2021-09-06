package com.bank.repository;

import com.bank.dto.ObjectDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.PagingAndSortingRepository;

import java.math.BigInteger;
import java.util.Date;

public interface ObjectsRepository extends PagingAndSortingRepository<ObjectDto, BigInteger> {
    ObjectDto findByObjectId(BigInteger objectId);
    ObjectDto findByParentIdAndObjectType(BigInteger parentId, Integer objectType);
    Page<ObjectDto> findByParentIdAndObjectTypeAndObjectDocBetween(
            BigInteger parentId, Integer objectType, Date startDateObjectDoc,
            Date endDateObjectDoc, Pageable pageRequest);
}
