package com.bank.repository;

import com.bank.dto.ObjectDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.PagingAndSortingRepository;

import java.math.BigInteger;
import java.sql.Date;
import java.util.List;

public interface ObjectsRepository extends PagingAndSortingRepository<ObjectDto, BigInteger> {
    ObjectDto findByObjectId(BigInteger objectId);
    ObjectDto findByParentIdAndObjectName(BigInteger parentId, String objectName);
    Page<ObjectDto> findByParentIdAndObjectNameAndObjectDocBetween(
            BigInteger parentId, String objectName, Date ObjectDocStart,
            Date ObjectDocEnd, Pageable pageRequest);
    List<ObjectDto> findByObjectNameAndParentId(String objectName, BigInteger ParentId);
}
