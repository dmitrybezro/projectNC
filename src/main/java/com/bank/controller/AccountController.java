package com.bank.controller;

import com.bank.entity.Account;
import com.bank.entity.Task;
import com.bank.entity.Transaction;
import com.bank.exception.AccountNotFoundException;
import com.bank.exception.NegativeAccountBalanceException;
import com.bank.model.TransferRequestIn;
import com.bank.service.AccountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigInteger;
import java.util.Date;
import java.util.List;

@RestController
@RequestMapping("/api")
public class AccountController {

    @Autowired
    AccountService accountService;

    @GetMapping("/accounts")
    public ResponseEntity<List<Account>> getAllAccount(@RequestParam BigInteger objectId)throws IllegalAccessException, InstantiationException {
        return ResponseEntity.ok(accountService.getAccountList(objectId));
    }

    @GetMapping("/account/{id}")
    public ResponseEntity<Account> getOneAccount(@PathVariable BigInteger id) throws AccountNotFoundException, IllegalAccessException, InstantiationException {
        return ResponseEntity.ok(accountService.getGeneralInfo(id));
    }

    @GetMapping("/account/{id}/operations")
    public ResponseEntity<List<Transaction>> getListTransaction(@PathVariable BigInteger id, @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) Date start_date,
                                                    @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) Date end_date, @RequestParam(defaultValue = "1") Integer page,
                                                    @RequestParam(defaultValue = "10") Integer items) throws Exception {

        List<Transaction> list = accountService.getList(id,start_date, end_date, page, items);
        return new ResponseEntity<List<Transaction>>(list, HttpStatus.OK);
    }

    @PostMapping("/transfer")
    public ResponseEntity<Task> transfer(@RequestBody TransferRequestIn task) throws AccountNotFoundException, NegativeAccountBalanceException, IllegalAccessException, InstantiationException {
        return ResponseEntity.ok(accountService.transfer(task));
    }
    @GetMapping("/transfer/{id}")
    public ResponseEntity<Task> getTransactionStatus(@PathVariable BigInteger id) throws IllegalAccessException, InstantiationException {
        return ResponseEntity.ok(accountService.getTransactionInfo(id));
    }
}
