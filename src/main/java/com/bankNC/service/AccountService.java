package com.bankNC.service;

import com.bankNC.converters.EntityDtoConverter;
import com.bankNC.dto.ObjectDto;
import com.bankNC.dto.ValueDto;
import com.bankNC.entity.Account;
import com.bankNC.entity.Customer;
import com.bankNC.exception.AccountNotFoundException;
import com.bankNC.exception.CustomerNotFoundException;
import com.bankNC.repository.ObjectsRepository;
import com.bankNC.repository.ValuesRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;

@Service
public class AccountService {
    @Autowired
    private ObjectsRepository objectsRepository;

    @Autowired
    private ValuesRepository valueRepository;

    public Account getOne(BigInteger id) throws AccountNotFoundException, IllegalAccessException, InstantiationException {

        ObjectDto objectDto = objectsRepository.findByObjectId(id);
        List<ValueDto> listAttr = new ArrayList<>();

        listAttr = valueRepository.findAllByObjectId(id);

        if(listAttr.size() == 0){
            throw new AccountNotFoundException("Пользователь не найден");
        }

        Account account = new Account();
        account = EntityDtoConverter.toEntity(new EntityDtoConverter.Pair<ObjectDto, List<ValueDto>>(objectDto, listAttr), Account.class);
        account.setName("Account");
        return account;
    }
}
