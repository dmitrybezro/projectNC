package com.bankNC.controller;

import com.bankNC.entity.Operation;
import com.bankNC.exception.AccountNotFoundException;
import com.bankNC.exception.NegativeAccountBalanceException;
import com.bankNC.model.TransferRequestModel;
import com.bankNC.service.AccountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@RestController
@RequestMapping("/api")
public class AccountController {

    @Autowired
    AccountService accountService;

    @GetMapping("/account/{id}")
    public ResponseEntity getOneAccount(@PathVariable BigInteger id){
        try {
            return ResponseEntity.ok(accountService.getGeneralInfo(id));
        }catch (AccountNotFoundException exception){
            return ResponseEntity.badRequest().body(exception.getMessage());
        } catch (Exception exception){
            return ResponseEntity.badRequest().body(exception.getMessage());
        }
    }

    @GetMapping("/account/{id}/operations")
    public ResponseEntity<List<Operation>> getOne(@PathVariable BigInteger id, @RequestParam Date start_date,
                                                  @RequestParam Date end_date, @RequestParam(defaultValue = "1") Integer page,
                                                  @RequestParam(defaultValue = "10") Integer items){
        try {
            return ResponseEntity.ok(accountService.getTransferInfo(id,start_date, end_date, page, items));
        }/*catch (AccountNotFoundException exception){
          //  return ResponseEntity.badRequest().body(exception.getMessage());
        }*/ catch (Exception exception){
/*            Operation operation = new Operation();
            List<Operation> list = new ArrayList<>();
            list.add(operation);*/
            ResponseEntity<List<Operation>> responseEntity = new ResponseEntity<List<Operation>>(HttpStatus.BAD_REQUEST);
           // return ResponseEntity.badRequest().body(exception.getMessage());
            //return ResponseEntity.badRequest().body(exception.getMessage());
            return responseEntity;
        }
    }

    @PostMapping("/transfer")
    public ResponseEntity transfer(@RequestBody Operation operation){
        try {
            return ResponseEntity.ok(accountService.transfer(operation));
        }catch (NegativeAccountBalanceException exception){
            return ResponseEntity.badRequest().body(exception.getTr());
        } catch (AccountNotFoundException exception){
            return ResponseEntity.badRequest().body(exception.getTr());
        }
        catch (Exception exception){
            return ResponseEntity.badRequest().body(exception.getMessage());
        }
    }

    @GetMapping("/transfer/{id}")
    public ResponseEntity getTransaction(@PathVariable BigInteger id){
        try {
            return ResponseEntity.ok(accountService.getTransactionInfo(id));
        } catch (Exception exception){
            return ResponseEntity.badRequest().body(exception.getMessage());
        }
    }
}
