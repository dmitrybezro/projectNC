package com.bank.controller;

import com.bank.entity.Account;
import com.bank.entity.Transaction;
import com.bank.exception.AccountNotFoundException;
import com.bank.model.TransferRequestIn;
import com.bank.service.AccountService;
import org.springframework.beans.factory.annotation.Autowired;
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

    @GetMapping("/account/{id}")
    public ResponseEntity<Account> getOneAccount(@PathVariable BigInteger id) throws AccountNotFoundException, IllegalAccessException, InstantiationException {
      //  try {
            return ResponseEntity.ok(accountService.getGeneralInfo(id));
/*        }catch (AccountNotFoundException exception){
            return ResponseEntity.badRequest().body(exception.getMessage());
        } catch (Exception exception){
            return ResponseEntity.badRequest().body(exception.getMessage());
        }*/
    }

    @GetMapping("/account/{id}/operations")
    public ResponseEntity<List<Transaction>> getListTransaction(@PathVariable BigInteger id, @RequestParam Date start_date,
                                                    @RequestParam Date end_date, @RequestParam(defaultValue = "1") Integer page,
                                                    @RequestParam(defaultValue = "10") Integer items){
        try {
            List<Transaction> list = accountService.getList(id,start_date, end_date, page, items);
            return new ResponseEntity<List<Transaction>>(list, HttpStatus.OK);
        }catch (Exception exception){

            return new ResponseEntity<List<Transaction>>(HttpStatus.BAD_REQUEST);
        }
    }

    @PostMapping("/transfer")
    public ResponseEntity transfer(@RequestBody TransferRequestIn task){
        try {
            return ResponseEntity.ok(accountService.transfer(task));
        }
        catch (Exception exception){
            return ResponseEntity.badRequest().body(exception.getMessage());
        }
    }
    @GetMapping("/transfer/{id}")
    public ResponseEntity getTransactionStatus(@PathVariable BigInteger id){
        try {
            return ResponseEntity.ok(accountService.getTransactionInfo(id));
        } catch (Exception exception){
            return ResponseEntity.badRequest().body(exception.getMessage());
        }
    }
}
