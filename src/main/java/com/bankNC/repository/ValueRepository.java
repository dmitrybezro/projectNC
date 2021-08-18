package com.bankNC.repository;

import com.bankNC.entity.forTables.ValueDto;
import org.springframework.data.repository.CrudRepository;

import java.util.ArrayList;
import java.util.List;

public interface ValueRepository  extends CrudRepository<ValueDto, Integer> {
    ValueDto findByAttrIdAndObjId(Integer attrId, Integer objId);
    ArrayList<ValueDto> findFirst5ByObjId(Integer objId);
    List<ValueDto> findByObjId(Integer objId);
}
