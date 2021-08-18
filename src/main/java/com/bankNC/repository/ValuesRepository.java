package com.bankNC.repository;

import com.bankNC.entity.forTables.ValueDto;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface ValuesRepository  extends CrudRepository<ValueDto, Integer> {
    List<ValueDto> findAllByObjid(Integer objid);
}
