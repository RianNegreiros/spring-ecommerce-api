package com.riannegreiros.springecommerce.modules.customer.controller;

import com.riannegreiros.springecommerce.modules.customer.entity.Customer;
import com.riannegreiros.springecommerce.modules.customer.service.CustomerService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/api/customer")
public class CustomerController {

    private final CustomerService customerService;

    public CustomerController(CustomerService customerService) {
        this.customerService = customerService;
    }

    @PostMapping
    public ResponseEntity<Customer> save(@RequestBody Customer customer) {
        Customer savedCustomer = customerService.save(customer);
        return new ResponseEntity<>(savedCustomer, HttpStatus.CREATED);
    }

    @PatchMapping("/{id}")
    public ResponseEntity<String> enable(@PathVariable(name = "id")UUID id) {
        customerService.enable(id);
        return new ResponseEntity<>("Customer has been successfully enabled", HttpStatus.OK);
    }
}
