package com.bank.controller;

import com.bank.configuration.CustomUserDetails;
import com.bank.entity.Account;
import com.bank.entity.Task;
import com.bank.entity.Transaction;
import com.bank.model.TransferRequestIn;
import com.bank.service.AccountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.math.BigInteger;
import java.util.Date;
import java.util.List;

@RestController
@RequestMapping("/api")
public class AccountController {

    @Autowired
    AccountService accountService;

    @GetMapping("/accounts")
    public ResponseEntity<List<Account>> getAllAccount(Authentication authentication)
            throws IllegalAccessException, InstantiationException {
        CustomUserDetails ud = (CustomUserDetails) authentication.getPrincipal();
        return ResponseEntity.ok(accountService.getAccountList(ud.getUserId()));
    }

    @GetMapping("/account/{id}")
    public ResponseEntity<Account> getOneAccount(@PathVariable BigInteger id,
                                                 Authentication authentication)
            throws IllegalAccessException, InstantiationException {
        CustomUserDetails ud = (CustomUserDetails) authentication.getPrincipal();
        Account account = accountService.getAccountInfoByUser(id, ud.getUserId());
        if(account == null){
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Account not found");
        }
        ResponseEntity.internalServerError();
        return ResponseEntity.ok(account);
    }

    @GetMapping("/account/{id}/operations")
    public ResponseEntity<List<Transaction>> getListTransaction(
            @PathVariable BigInteger id,
            @RequestParam(defaultValue = "2021-09-01") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) Date start_date,
            @RequestParam(defaultValue = "2021-09-30") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) Date end_date,
            @RequestParam(defaultValue = "0") Integer page,
            @RequestParam(defaultValue = "10") Integer items) throws Exception {

        CustomUserDetails ud = (CustomUserDetails) SecurityContextHolder
                .getContext().getAuthentication().getPrincipal();
        List<Transaction> list = accountService.getListByUser(id, ud.getUserId(), start_date, end_date, page, items);
        if(list == null){
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Account not found");
        }
        ResponseEntity.internalServerError();
        return ResponseEntity.ok(list);
    }

    @PostMapping("/transfer")
    public ResponseEntity<Task> transfer(@RequestBody TransferRequestIn task) throws Exception {
        CustomUserDetails ud = (CustomUserDetails) SecurityContextHolder
                .getContext().getAuthentication().getPrincipal();
        Task taskReturned = accountService.transfer(task, ud.getUserId());
        if(taskReturned == null){
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Account not found");
        }
        ResponseEntity.internalServerError();
        return ResponseEntity.ok(taskReturned);

    }
    @GetMapping("/transfer/{id}")
    public ResponseEntity<Task> getTransactionStatus(@PathVariable BigInteger id)
            throws IllegalAccessException, InstantiationException {
        CustomUserDetails ud = (CustomUserDetails) SecurityContextHolder
                .getContext().getAuthentication().getPrincipal();
        Task task = accountService.getTransactionInfo(id, ud.getUserId());
        if(task == null){
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Account not found");
        }
        ResponseEntity.internalServerError();
        return ResponseEntity.ok(task);
    }
}
