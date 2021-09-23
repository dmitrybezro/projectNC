package com.bank.service;

import com.bank.dto.ObjectDto;
import com.bank.entity.Account;
import com.bank.entity.Task;
import com.bank.entity.Transaction;
import com.bank.model.TransferRequestIn;
import com.bank.repository.ObjectsRepository;
import com.bank.repository.ValuesRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.interceptor.TransactionAspectSupport;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Service
public class AccountService {
    @Autowired
    private ObjectsRepository objectsRepository;

    @Autowired
    private ValuesRepository valueRepository;

    @Autowired
    private EntityService entityService;

    public List<Account> getAccountList(BigInteger id) throws IllegalAccessException, InstantiationException {
        List<ObjectDto> objectDtoList = objectsRepository.findByObjectTypeAndParentId("account", id);
        List<Account> accountList= new ArrayList<Account>();

        for(ObjectDto objectDto : objectDtoList){
            accountList.add(entityService.getById(objectDto.getObjectId(), Account.class));
        }

        return accountList;
    }

    public Account getAccountInfoByUser(BigInteger accountId, BigInteger userId) throws IllegalAccessException, InstantiationException {
        return entityService.getByIdAndParentId(accountId, userId, Account.class);
    }

    public List<Transaction> getListByUser(BigInteger accountId, BigInteger userId, Date start_date,
                                     Date end_date, Integer page, Integer items) throws Exception {

        if(entityService.getByIdAndParentId(accountId, userId, Account.class) == null){
            return null;
        }

        PageRequest pageRequest = PageRequest.of(page, items);

        Page<ObjectDto> pageDto = objectsRepository.findByParentIdAndObjectTypeAndObjectDocBetween(accountId, "transaction",
                new java.sql.Date(start_date.getTime()), new java.sql.Date(end_date.getTime()), pageRequest);
        List<Transaction> transactionsList = new ArrayList<>();

        for(ObjectDto object: pageDto){
            transactionsList.add(entityService.getById(object.getObjectId(),Transaction.class));
        }

        return transactionsList;
    }

   public Task transfer(TransferRequestIn input, BigInteger userId)throws IllegalAccessException, InstantiationException{

       if(entityService.getByIdAndParentId(input.getIdAccountSend(), userId, Account.class) == null){
           return null;
       }

       BigInteger idAccountSend = input.getIdAccountSend();
       BigInteger idAccountReceive = input.getIdAccountReceive();
       Double transferAmount = input.getSum();

       //  Create task
       Task currentTask = new Task(idAccountSend, idAccountReceive, "New", transferAmount);

       BigInteger idTask = entityService.saveEntity(currentTask);
       currentTask.setId(idTask);

       //  вернуть таск

       //  Build account send
       Account accountSend = entityService.getById(idAccountSend, Account.class);
       if(accountSend.getBalance() < transferAmount){
           currentTask.setStatus("Error");
           currentTask.setErrorMessage("There are not enough funds on the account");
           entityService.saveEntity(currentTask);
           return currentTask;
       }

       //  Build account receive
       Account accountReceive = entityService.getById(idAccountReceive, Account.class);
       if(!accountSend.getCurrency().equals(accountReceive.getCurrency())){
           currentTask.setErrorMessage("The account currencies are different");
           currentTask.setStatus("Error");
           entityService.saveEntity(currentTask);
           return currentTask;
       }

      Runnable runnable = () -> {
           try{
               transferThread(accountSend, accountReceive, currentTask, idAccountSend, idAccountReceive, transferAmount);
           } catch (Exception exception){
               exception.printStackTrace();
           }
      };
//      ExecutorService executorService = Executors.newSingleThreadExecutor();
//      executorService.submit(runnable).get();
      Thread thread = new Thread(runnable);
      thread.start();

      return currentTask;
   }

    @Transactional
    private String transferAttempt(Account accountSend, Account accountReceive,
                                    Account accountDraft, Double transferAmount) throws IllegalAccessException {

        //  Reduce balance on sender`s account
        accountSend.setBalance(accountSend.getBalance() - transferAmount);

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

    private void transferThread(Account accountSend,Account accountReceive, Task currentTask, BigInteger idAccountSend,
                                BigInteger idAccountReceive, Double transferAmount) throws IllegalAccessException, InstantiationException {

        //  Build account draft
        Account accountDraft = entityService.getByParentId(idAccountSend, Account.class);
        currentTask.setStatus("InProcess");
        entityService.updateEntity(currentTask);
        String errorMessage = transferAttempt(accountSend, accountReceive, accountDraft, transferAmount);
        if(errorMessage.equals("")) {
            currentTask.setStatus("Success");
            entityService.updateEntity(currentTask);

            entityService.saveTransaction(new Transaction("OUT", transferAmount, new java.sql.Date(System.currentTimeMillis())), idAccountSend);
            entityService.saveTransaction(new Transaction("IN", transferAmount, new java.sql.Date(System.currentTimeMillis())), idAccountReceive);
        } else{
            currentTask.setStatus("Error");
            entityService.saveEntity(currentTask);
        }
    }

    public Task getTransactionInfo(BigInteger taskId, BigInteger userId) throws IllegalAccessException, InstantiationException {
        Task task = entityService.getById(taskId, Task.class);
        Account account = entityService.getById(task.getParentId(), Account.class);

        if(!account.getParentId().equals(userId)){
            return null;
        } else{
            return task;
        }
    }
}
