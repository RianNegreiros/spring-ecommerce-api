package com.riannegreiros.springecommerce.modules.customer.controller;

import com.riannegreiros.springecommerce.modules.customer.email.service.EmailService;
import com.riannegreiros.springecommerce.modules.customer.entity.Customer;
import com.riannegreiros.springecommerce.modules.customer.service.CustomerService;
import com.riannegreiros.springecommerce.modules.user.entity.User;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

import static com.riannegreiros.springecommerce.modules.customer.email.util.EmailUtil.buildEmail;

@Api(value = "CRUD for customer resource", tags = {"Customer Controller"})
@RestController
@RequestMapping("/api/customers")
public class CustomerController {

    private final CustomerService customerService;
    private final EmailService emailService;

    public CustomerController(CustomerService customerService, EmailService emailService) {
        this.customerService = customerService;
        this.emailService = emailService;
    }

    @ApiOperation(value = "Create a new customer")
    @PostMapping
    public ResponseEntity<String> save(@RequestBody Customer customer) {
        String token = customerService.save(customer);
        String link = "http://localhost:8080/api/token/confirm?token=" + token;
        emailService.send(
                customer.getEmail(),
                buildEmail(customer.getFirstName(), link));
        return new ResponseEntity<>("Customer has been successfully created. Waiting for E-mail confirmation.", HttpStatus.CREATED);
    }

    @ApiOperation(value = "Update a customer")
    @PutMapping("/{id}")
    public ResponseEntity<Customer> update(@RequestBody Customer customer, @PathVariable(name = "id")UUID id) {
        Customer updatedCustomer = customerService.update(customer, id);
        return new ResponseEntity<>(updatedCustomer, HttpStatus.OK);
    }

    @ApiOperation(value = "Enable customer")
    @PatchMapping("/{id}")
    public ResponseEntity<String> enable(@PathVariable(name = "id") UUID id) {
        customerService.enable(id);
        return new ResponseEntity<>("Customer has been successfully enabled.", HttpStatus.OK);
    }
}
