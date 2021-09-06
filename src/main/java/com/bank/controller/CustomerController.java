package com.bank.controller;

/*import com.bankNC.converters.CustomerTo;*/

import com.bank.entity.Customer;
import com.bank.exception.CustomerNotFoundException;
import com.bank.service.CustomerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigInteger;

@RestController
@RequestMapping("/api")
public class CustomerController {

    @Autowired
    private CustomerService customerService;

    @PostMapping("/save")
    public ResponseEntity save(@RequestBody Customer customer){
        try {
            customerService.AddCustomer(customer);
            return ResponseEntity.ok("Объект сохранен " );
        }catch (Exception exception){
            return ResponseEntity.badRequest().body(exception.getMessage());
        }
    }

/*    @GetMapping("/users")
    public ResponseEntity getUsers() {
        try {
            return ResponseEntity.ok("Сервер работает");
        }catch (Exception exception){
            return ResponseEntity.badRequest().body("Произошла ошибка");
        }
    }*/

    @GetMapping("/users")
    public ResponseEntity getOneCustomer(@RequestParam BigInteger objectId){
        try {
            return ResponseEntity.ok(customerService.getOne(objectId));
        }catch (CustomerNotFoundException exception){
            return ResponseEntity.badRequest().body(exception.getMessage());
        } catch (Exception exception){
        return ResponseEntity.badRequest().body(exception.getMessage());
        }
    }
}
