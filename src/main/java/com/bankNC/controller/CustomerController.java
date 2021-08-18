package com.bankNC.controller;

import com.bankNC.converters.CustomerTo;
import com.bankNC.entity.Customer;
import com.bankNC.entity.forTables.ObjectDto;
import com.bankNC.entity.forTables.ValueDto;

import com.bankNC.exception.CustomerNotFoundException;
import com.bankNC.service.CustomerService;
import jdk.jfr.Unsigned;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

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
            return ResponseEntity.badRequest().body("Произошла ошибка");
        }
    }

    @GetMapping("/users")
    public ResponseEntity getUsers() {
        try {
            return ResponseEntity.ok("Сервер работает");
        }catch (Exception exception){
            return ResponseEntity.badRequest().body("Произошла ошибка");
        }
    }

    @GetMapping("/room")
    public ResponseEntity getOneCustomer(@RequestParam Integer objid){
        try {
            return ResponseEntity.ok(customerService.getOne(objid));
        }catch (CustomerNotFoundException exception){
            return ResponseEntity.badRequest().body(exception.getMessage());
        } catch (Exception exception){
        return ResponseEntity.badRequest().body(exception.getMessage());
        }
    }
}
