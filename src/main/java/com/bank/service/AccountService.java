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

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.Executor;
import java.util.concurrent.TimeUnit;


@Service
public class AccountService {
    @Autowired
    Executor executor;

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
        int p =3 ;
        return transactionsList;
    }

    public Task transfer(TransferRequestIn request, BigInteger userId) throws Exception {
        Task task = new Task(
                request.getIdAccountSend(),
                request.getIdAccountReceive(),
                "New",
                request.getSum());

        BigInteger taskId = entityService.saveEntity(task);
        task.setId(taskId);

        executor.execute(()-> {
            try {
                processTask(task, userId);
            } catch (Exception exception) {
                exception.printStackTrace();
            }
        });
        return task;
    }

    public void processTask(Task task, BigInteger userId) throws Exception {
        //  Build accounts
        Account senderAccount;
        Account draftAccount;
        Account receiverAccount;
        try {
            senderAccount = entityService.getByIdAndParentId(
                    task.getIdAccountSend(),
                    userId,
                    Account.class);

            draftAccount = entityService.getByParentId(
                    task.getIdAccountSend(),
                    Account.class);

            receiverAccount = entityService.getById(
                    task.getIdAccountReceive(),
                    Account.class);
        } catch (Exception e){
            failTask(task, e.getMessage());
            return;
        }


        String validationMessage = validateTask(senderAccount, receiverAccount, task);
        if(validationMessage != null) {
            failTask(task, validationMessage);
            return;
        }

        String transferErr = null;
        int toDraftAttempts = 0;
        boolean isToDraftSuccess = false;

        while(toDraftAttempts < 3 && !isToDraftSuccess){
            try {
                isToDraftSuccess = transferToDraft(task, senderAccount, draftAccount);
            } catch (Exception e){
                transferErr = e.getMessage();
            }
            toDraftAttempts++;
        }

        if(!isToDraftSuccess) {
            failTask(task, transferErr);
            return;
        }

        int toRecipientAttempts = 0;
        boolean isToRecipientSuccess = false;

        while(toRecipientAttempts < 3 && !isToRecipientSuccess){
            try {
                isToRecipientSuccess = transferToRecipient(task, draftAccount, senderAccount);
            } catch (Exception e){
                transferErr = e.getMessage();
            }
            toRecipientAttempts++;
        }

        if(!isToRecipientSuccess) {
            rollbackTransferToDraft(task, senderAccount, draftAccount);
            failTask(task, transferErr);
        }
    }

    private String validateTask(Account sender, Account receiver, Task task){
        if(sender == null) {
            return "Sender Account not found";
        }

        if(receiver == null) {
            return "Receiver Account not found";
        }

        if (sender.getBalance() < task.getTransferAmount()) {
            return "There are not enough funds on the account";
        }

        return null;
    }

    private void failTask(Task task, String message) throws Exception{
        task.setStatus("Error");
        task.setErrorMessage(message);
        entityService.saveEntity(task);
    }

    @Transactional
    private boolean transferToDraft(Task task, Account accountSender, Account draftAccount) throws Exception{
        accountSender.setBalance(accountSender.getBalance() - task.getTransferAmount());
        draftAccount.setBalance(draftAccount.getBalance() + task.getTransferAmount());
        entityService.saveEntity(accountSender);
        entityService.saveEntity(draftAccount);
        return true;
    }

    @Transactional
    private boolean transferToRecipient(Task task, Account draftAccount, Account recipientAccount) throws Exception{
        recipientAccount.setBalance(recipientAccount.getBalance() + task.getTransferAmount());
        draftAccount.setBalance(draftAccount.getBalance() - task.getTransferAmount());
        Transaction transactionOut = new Transaction("OUT", task.getTransferAmount(), task.getIdAccountReceive());
        transactionOut.setParentId(draftAccount.getParentId());
        Transaction transactionIn = new Transaction("IN", task.getTransferAmount(), task.getIdAccountSend());
        transactionIn.setParentId(task.getIdAccountReceive());
        task.setStatus("Success");
        entityService.saveEntity(recipientAccount);
        entityService.saveEntity(draftAccount);
        entityService.saveEntity(transactionIn);
        entityService.saveEntity(transactionOut);
        entityService.saveEntity(task);
        return true;
    }

    @Transactional
    private boolean rollbackTransferToDraft(Task task, Account accountSender, Account draftAccount) throws Exception{
        accountSender.setBalance(accountSender.getBalance() + task.getTransferAmount());
        draftAccount.setBalance(draftAccount.getBalance() - task.getTransferAmount());
        entityService.saveEntity(accountSender);
        entityService.saveEntity(draftAccount);
        return true;
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
