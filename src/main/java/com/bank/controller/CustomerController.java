package com.bank.controller;

import com.bank.configuration.CustomUserDetails;
import com.bank.entity.Customer;
import com.bank.exception.CustomerNotFoundException;
import com.bank.service.CustomerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.math.BigInteger;

@RestController
@RequestMapping("/api")
public class CustomerController {

    @Autowired
    private CustomerService customerService;

    @GetMapping("/user")
    public ResponseEntity<Customer> getCustomer() throws IllegalAccessException, InstantiationException, CustomerNotFoundException {
        CustomUserDetails ud = (CustomUserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        return ResponseEntity.ok(customerService.getOne(ud.getUserId()));
    }
}
