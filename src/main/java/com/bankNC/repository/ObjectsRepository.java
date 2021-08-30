package com.bankNC.repository;

import com.bankNC.dto.ObjectDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.PagingAndSortingRepository;

import java.math.BigInteger;
import java.util.Date;
import java.util.List;

public interface ObjectsRepository extends PagingAndSortingRepository<ObjectDto, BigInteger> {
    ObjectDto findByObjectId(BigInteger objectId);
    ObjectDto findByParentIdAndObjectType(BigInteger parentId, Integer objectType);
    Page<ObjectDto> findByParentIdAndObjectTypeAndObjectDocBetween(
            BigInteger parentId, Integer objectType, Date startDateObjectDoc,
            Date endDateObjectDoc, Pageable pageRequest);
}
