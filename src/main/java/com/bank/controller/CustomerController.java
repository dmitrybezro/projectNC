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

    @GetMapping("/users")
    public ResponseEntity<Customer> getOneCustomer(@RequestParam BigInteger objectId) throws CustomerNotFoundException, IllegalAccessException, InstantiationException {
            return ResponseEntity.ok(customerService.getOne(objectId));
    }
}
