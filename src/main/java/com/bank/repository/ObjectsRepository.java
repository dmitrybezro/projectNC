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
    ObjectDto findByParentIdAndObjectType(BigInteger parentId, String objectType);
    Page<ObjectDto> findByParentIdAndObjectTypeAndObjectDocBetween(
            BigInteger parentId, String objectType, Date ObjectDocStart,
            Date ObjectDocEnd, Pageable pageRequest);
    List<ObjectDto> findByObjectTypeAndParentId(String objectType, BigInteger ParentId);
    ObjectDto findByObjectIdAndParentId(BigInteger objectId, BigInteger parentId);
}
