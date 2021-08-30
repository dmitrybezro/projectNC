package com.bankNC.service;

import com.bankNC.converters.EntityDtoConverter;
import com.bankNC.dto.ObjectDto;
import com.bankNC.dto.ValueDto;
import com.bankNC.entity.Account;
import com.bankNC.entity.Task;
import com.bankNC.entity.Transaction;
import com.bankNC.exception.AccountNotFoundException;
import com.bankNC.exception.NegativeAccountBalanceException;
import com.bankNC.model.TransferRequestIn;
import com.bankNC.repository.ObjectsRepository;
import com.bankNC.repository.ValuesRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.interceptor.TransactionAspectSupport;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service
public class AccountService {
    @Autowired
    private ObjectsRepository objectsRepository;

    @Autowired
    private ValuesRepository valueRepository;

    @Autowired
    private EntityService entityService;

    public Account getGeneralInfo(BigInteger id) throws AccountNotFoundException, IllegalAccessException, InstantiationException {

        ObjectDto objectDto = objectsRepository.findByObjectId(id);
        List<ValueDto> listAttr = valueRepository.findAllByObjectId(id);

        if(listAttr.size() == 0){
            throw new AccountNotFoundException("Счет не найден");
        }

        Account account = EntityDtoConverter.toEntity(objectDto, listAttr, Account.class);
        account.setName("Account");
        return account;
    }

    public List<Transaction> getList(BigInteger id, Date start_date,
                                           Date end_date, Integer page, Integer items) throws IllegalAccessException, InstantiationException{

        PageRequest pageRequest = PageRequest.of(page, items);
        Page<ObjectDto> pageDto = objectsRepository.findByParentIdAndObjectTypeAndObjectDocBetween(id, 4, start_date, end_date, pageRequest);

        List<Transaction> transactionsList = new ArrayList<>();
        for(ObjectDto object: pageDto){
            transactionsList.add(entityService.getById(object.getObjectId(),Transaction.class));
        }
        return transactionsList;
    }

   public Task transfer(TransferRequestIn input)throws AccountNotFoundException, NegativeAccountBalanceException, IllegalAccessException, InstantiationException{

        BigInteger idAccountSend = input.getIdAccountSend();
        BigInteger idAccountReceive = input.getIdAccountReceive();
        Double transferAmount = input.getSum();

        //  Create task
        Task currentTask = new Task(idAccountSend, idAccountReceive, "Lilu", transferAmount);
        BigInteger idTask = entityService.saveEntity(currentTask);

        //  Build account send
        Account accountSend = entityService.getById(idAccountSend, Account.class);
        if(accountSend.getBalance() < transferAmount){
            throw new NegativeAccountBalanceException("There are not enough funds on the account");
        }

        //  Build account receive
        Account accountReceive = entityService.getById(idAccountReceive, Account.class);

        //  Build account draft
        Account accountDraft = entityService.getByParentId(idAccountSend, Account.class);

        Task returnedTask = new Task("New", "");
        returnedTask.setId(idTask);

        String errorMessage = transferAttempt(accountSend, accountReceive, accountDraft, transferAmount);

        if(errorMessage.equals("")) {

            currentTask.setStatus("Success");
            entityService.saveEntity(currentTask);

            entityService.saveTransaction(new Transaction("OUT", transferAmount, new Date()), idAccountSend);
            entityService.saveTransaction(new Transaction("IN", transferAmount, new Date()), idAccountReceive);

        } else{
            currentTask.setStatus("Error");
            entityService.saveEntity(currentTask);

            returnedTask.setStatus("Error");
            returnedTask.setErrorMessage(errorMessage);
        }

       return returnedTask;
    }

    @Transactional
    private String transferAttempt(Account accountSend, Account accountReceive,
                                    Account accountDraft, Double transferAmount) throws IllegalAccessException, NegativeAccountBalanceException {

        //  Reduce balance on sender`s account
        accountSend.setBalance(accountSend.getBalance() - transferAmount);
        //  Где-то здесь косяк
        entityService.updateEntity(accountSend);

        String message = "";

        for(int attempt = 1; attempt <= 3; attempt++) {
            try {
                //  Increase balance on draft`s account
                accountDraft.setBalance(accountDraft.getBalance() - transferAmount);
                entityService.updateEntity(accountDraft);

                //  Reduce balance on receive`s account
                accountReceive.setBalance(accountReceive.getBalance() + transferAmount);
                entityService.updateEntity(accountReceive);

                return message;
            } catch (Exception e) {
                if(attempt == 3){
                    TransactionAspectSupport.currentTransactionStatus()
                            .setRollbackOnly();
                    message = e.getMessage();
                }
            }
        }
        return message;
    }
    public Task getTransactionInfo(BigInteger id)
            throws IllegalAccessException,
                InstantiationException {

        return  entityService.getById(id, Task.class);
    }
}
