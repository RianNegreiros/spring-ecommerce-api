package com.riannegreiros.springecommerce.modules.customer.controller;

import com.riannegreiros.springecommerce.modules.customer.email.service.EmailService;
import com.riannegreiros.springecommerce.modules.customer.entity.Customer;
import com.riannegreiros.springecommerce.modules.customer.service.CustomerService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

import static com.riannegreiros.springecommerce.modules.customer.email.util.EmailUtil.buildEmail;

@RestController
@RequestMapping("/api/customer")
public class CustomerController {

    private final CustomerService customerService;
    private final EmailService emailService;

    public CustomerController(CustomerService customerService, EmailService emailService) {
        this.customerService = customerService;
        this.emailService = emailService;
    }

    @PostMapping
    public ResponseEntity<String> save(@RequestBody Customer customer) {
        String token = customerService.save(customer);
        String link = "http://localhost:8080/api/token/confirm?token=" + token;
        emailService.send(
                customer.getEmail(),
                buildEmail(customer.getFirstName(), link));
        return new ResponseEntity<>("Customer has been successfully created. Waiting for E-mail confirmation.", HttpStatus.CREATED);
    }

    @PatchMapping("/{id}")
    public ResponseEntity<String> enable(@PathVariable(name = "id")UUID id) {
        customerService.enable(id);
        return new ResponseEntity<>("Customer has been successfully enabled.", HttpStatus.OK);
    }
}
