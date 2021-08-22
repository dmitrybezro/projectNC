package com.bankNC.controller;

import com.bankNC.exception.AccountNotFoundException;
import com.bankNC.exception.CustomerNotFoundException;
import com.bankNC.service.AccountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.math.BigInteger;

@RestController
@RequestMapping("/api")
public class AccountController {

    @Autowired
    AccountService accountService;

    @GetMapping("/account/{id}")
    public ResponseEntity getOneAccount(@PathVariable BigInteger id){
        try {
            return ResponseEntity.ok(accountService.getOne(id));
        }catch (AccountNotFoundException exception){
            return ResponseEntity.badRequest().body(exception.getMessage());
        } catch (Exception exception){
            return ResponseEntity.badRequest().body(exception.getMessage());
        }
    }
}
