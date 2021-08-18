package com.bankNC.repository;

import com.bankNC.entity.forTables.ObjectDto;
import org.springframework.data.repository.CrudRepository;

public interface ObjectsRepository extends CrudRepository<ObjectDto, Integer> {
    @Override
    long count();
}
