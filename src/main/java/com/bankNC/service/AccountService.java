package com.bankNC.service;

import com.bankNC.converters.EntityDtoConverter;
import com.bankNC.dto.ObjectDto;
import com.bankNC.dto.ValueDto;
import com.bankNC.entity.Account;
import com.bankNC.entity.Operation;
import com.bankNC.exception.AccountNotFoundException;
import com.bankNC.exception.NegativeAccountBalanceException;
import com.bankNC.model.TransferRequestModel;
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

    public Account getGeneralInfo(BigInteger id) throws AccountNotFoundException, IllegalAccessException, InstantiationException {

        ObjectDto objectDto = objectsRepository.findByObjectId(id);
        List<ValueDto> listAttr = valueRepository.findAllByObjectId(id);

        if(listAttr.size() == 0){
            throw new AccountNotFoundException("Счет не найден");
        }

        Account account = new Account();
        account = EntityDtoConverter.toEntity(new EntityDtoConverter.Pair<ObjectDto, List<ValueDto>>(objectDto, listAttr), Account.class);
        account.setName("Account");
        return account;
    }

    public List<Operation> getTransferInfo(BigInteger id, Date start_date,
                                           Date end_date, Integer page, Integer items) throws AccountNotFoundException, IllegalAccessException, InstantiationException{

        List<ObjectDto> listOperationsDto = objectsRepository.findByParentId(id);

        if(listOperationsDto.size() == 0){
            throw new AccountNotFoundException("Счёт не найден");
        }

        List<List<ValueDto>> listAttributes = new ArrayList<>();
        List<Operation> listOperationsAll = new ArrayList<>();
        for(int i = 0; i < listOperationsDto.size(); i++){
            listAttributes.add(valueRepository.findAllByObjectId(id));
            Operation operation = EntityDtoConverter.toEntity(new EntityDtoConverter.Pair<ObjectDto, List<ValueDto>>(listOperationsDto.get(i), listAttributes.get(i)), Operation.class);
            listOperationsAll.add(operation);
        }

        List<Operation> listOperations = new ArrayList<>();
        for(int i = 0; i < listOperationsAll.size(); i++){
            Date date = listOperationsAll.get(i).getDataOperation();
            if(date.after(start_date) && date.before(end_date)){
                listOperations.add(listOperationsAll.get(i));
            }
        }

        PageRequest pageRequest = PageRequest.of(page, items);

        Integer start = (int)pageRequest.getOffset();
        Integer end = (int)(start + pageRequest.getPageSize()) > listOperations.size()?
                                    listOperations.size():start + pageRequest.getPageSize();
        Page<Operation> toReturn = new PageImpl<>(listOperations.subList(start, end), pageRequest, listOperations.size());
        return toReturn.getContent();
    }

    //  Привести в божеский вид, но работает, как хотелось бы
    @Transactional(noRollbackFor = {AccountNotFoundException.class, NegativeAccountBalanceException.class} )
    public TransferRequestModel transfer(Operation operation)throws AccountNotFoundException, NegativeAccountBalanceException, IllegalAccessException, InstantiationException{

        BigInteger idAccountSend = operation.getIdAccountSend();
        BigInteger idAccountReceive = operation.getIdAccountReceive();
        BigInteger idTransaction = operation.getId();

        Operation transfer = new Operation(idAccountSend, idAccountReceive, "New",
                operation.getTransferAmount(), null, null);
        ObjectDto objectDtoOperation = EntityDtoConverter.toDto(transfer).getKey();
        objectsRepository.save(objectDtoOperation);
        List<ValueDto> listValueOperation = EntityDtoConverter.toDto(transfer).getValue();
        for(int i = 0; i < listValueOperation.size(); i++){
            listValueOperation.get(i).setObjectId(objectDtoOperation.getObjectId());
        }
        valueRepository.saveAll(listValueOperation);

        //  Организовать проверку, что это счет пользователя

        ObjectDto objectDtoSend = objectsRepository.findByObjectId(idAccountSend);
        List<ValueDto> listValueSend = valueRepository.findAllByObjectId(idAccountSend);
        if(listValueSend.size() == 0){
            String message = "Счет отправитель не существует";
            transfer.setMessage(message);
            transfer.setStatus("Error");
            ObjectDto objectDto = EntityDtoConverter.toDto(transfer).getKey();
            objectDto.setObjectId(objectDtoOperation.getObjectId());
            objectDto.setObjectName("operation");
            objectsRepository.save(objectDto);
            listValueOperation = EntityDtoConverter.toDto(transfer).getValue();
            for(int i = 0; i < listValueOperation.size(); i++){
                listValueOperation.get(i).setObjectId(objectDtoOperation.getObjectId());
            }
            valueRepository.saveAll(listValueOperation);
            throw new AccountNotFoundException(message, idTransaction);

        }
        Account accountSend = EntityDtoConverter.toEntity(new EntityDtoConverter.Pair<ObjectDto, List<ValueDto>>(objectDtoSend, listValueSend), Account.class);

        if(accountSend.getBalance() < operation.getTransferAmount()){
            String message = "На счете недостаточно средств";
            transfer.setMessage(message);
            transfer.setStatus("Error");
            ObjectDto objectDto = EntityDtoConverter.toDto(transfer).getKey();
            objectDto.setObjectId(objectDtoOperation.getObjectId());
            objectDto.setObjectName("operation");
            objectsRepository.save(objectDto);
            listValueOperation = EntityDtoConverter.toDto(transfer).getValue();
            for(int i = 0; i < listValueOperation.size(); i++){
                listValueOperation.get(i).setObjectId(objectDtoOperation.getObjectId());
            }
            valueRepository.saveAll(listValueOperation);
            throw new NegativeAccountBalanceException(message, idTransaction);
        }

        ObjectDto objectDtoReceive = objectsRepository.findByObjectId(idAccountReceive);
        List<ValueDto> listValueReceive = valueRepository.findAllByObjectId(idAccountReceive);
        if(listValueReceive.size() == 0){
            String message = "Принимающий счет не существует";
            transfer.setMessage(message);
            transfer.setStatus("Error");
            ObjectDto objectDto = EntityDtoConverter.toDto(transfer).getKey();
            objectDto.setObjectId(objectDtoOperation.getObjectId());
            objectDto.setObjectName("operation");
            objectsRepository.save(objectDto);
            listValueOperation = EntityDtoConverter.toDto(transfer).getValue();
            for(int i = 0; i < listValueOperation.size(); i++){
                listValueOperation.get(i).setObjectId(objectDtoOperation.getObjectId());
            }
            valueRepository.saveAll(listValueOperation);
            throw new AccountNotFoundException(message, idTransaction);
        }
        Account accountReceive = EntityDtoConverter.toEntity(new EntityDtoConverter.Pair<ObjectDto, List<ValueDto>>(objectDtoReceive, listValueReceive), Account.class);

        Account draftAccount = new Account(accountSend);
        ObjectDto objectDtoDraft = new ObjectDto(objectDtoSend);

        objectsRepository.save(objectDtoDraft);
        List<ValueDto> listValueDraft = EntityDtoConverter.toDto(draftAccount).getValue();
        for(int i = 0; i < listValueDraft.size(); i++){
            listValueDraft.get(i).setObjectId(objectDtoDraft.getObjectId());
            valueRepository.save(listValueDraft.get(i));
        }

        //  Уменьшили баланс на счете отправителе
        accountSend.setBalance(accountSend.getBalance() - operation.getTransferAmount());
        objectDtoSend = EntityDtoConverter.toDto(accountSend).getKey();
        listValueSend = EntityDtoConverter.toDto(accountSend).getValue();
        objectsRepository.save(objectDtoSend);
        valueRepository.saveAll(listValueSend);


        //  В процессе
        transfer.setStatus("InProgress");
        ObjectDto objectDto = EntityDtoConverter.toDto(transfer).getKey();
        objectDto.setObjectId(objectDtoOperation.getObjectId());
        objectDto.setObjectName("operation");
        objectsRepository.save(objectDto);
        listValueOperation = EntityDtoConverter.toDto(transfer).getValue();
        for (ValueDto valueDto : listValueOperation) {
            valueDto.setObjectId(objectDtoOperation.getObjectId());
        }
        valueRepository.saveAll(listValueOperation);


        for(int attempt = 1; attempt <= 3; attempt++) {
            try {
                draftAccount.setBalance(draftAccount.getBalance() - operation.getTransferAmount());
                ObjectDto objectDtoDraft1 = EntityDtoConverter.toDto(draftAccount).getKey();
                List<ValueDto> listValueDraft1 = EntityDtoConverter.toDto(draftAccount).getValue();
                objectsRepository.save(objectDtoDraft1);
                for(int i = 0; i < listValueDraft1.size(); i++){
                    listValueDraft1.get(i).setObjectId(objectDtoDraft1.getObjectId());
                }
                valueRepository.saveAll(listValueDraft1);


                accountReceive.setBalance(accountReceive.getBalance() + operation.getTransferAmount());
                objectDtoReceive = EntityDtoConverter.toDto(accountReceive).getKey();
                listValueReceive = EntityDtoConverter.toDto(accountReceive).getValue();
                objectsRepository.save(objectDtoReceive);
                valueRepository.saveAll(listValueReceive);

                valueRepository.deleteAll(listValueDraft1);
                objectsRepository.delete(objectDtoDraft1);

                break;
            } catch (Exception e) {
                if(attempt == 3){
                    TransactionAspectSupport.currentTransactionStatus()
                            .setRollbackOnly();
                    return new TransferRequestModel(idTransaction, "Error", e.getMessage());
                }


            }
        }

        //  Завершена
        transfer.setStatus("Success");
        ObjectDto objectDto1 = EntityDtoConverter.toDto(transfer).getKey();
        objectDto1.setObjectId(objectDtoOperation.getObjectId());
        objectDto1.setObjectName("operation");
        objectsRepository.save(objectDto1);
        listValueOperation = EntityDtoConverter.toDto(transfer).getValue();
        for(int i = 0; i < listValueOperation.size(); i++){
            listValueOperation.get(i).setObjectId(objectDtoOperation.getObjectId());
        }
        valueRepository.saveAll(listValueOperation);

        valueRepository.deleteAll(listValueDraft);
        objectsRepository.delete(objectDtoDraft);
        return new TransferRequestModel(idTransaction, "New");
    }

    public TransferRequestModel getTransactionInfo(BigInteger id)
            throws NegativeAccountBalanceException,  IllegalAccessException,
                InstantiationException {
        ObjectDto objectDto = objectsRepository.findByObjectId(id);
        List<ValueDto> listValueOperation = valueRepository.findAllByObjectId(id);

        if(listValueOperation.size() == 0 ){
            throw new NegativeAccountBalanceException(" Lol " );
        }

        Operation operation = EntityDtoConverter.toEntity(
                new EntityDtoConverter.Pair<ObjectDto, List<ValueDto>>
                (objectDto, listValueOperation), Operation.class);
        return new TransferRequestModel(id, operation.getStatus(),  operation.getMessage());
    }
}
