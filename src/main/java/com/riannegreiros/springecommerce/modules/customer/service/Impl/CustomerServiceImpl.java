package com.riannegreiros.springecommerce.modules.customer.service.Impl;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.riannegreiros.springecommerce.exception.ResourceNotFoundException;
import com.riannegreiros.springecommerce.modules.customer.entity.Customer;
import com.riannegreiros.springecommerce.modules.customer.repository.CustomerRepository;
import com.riannegreiros.springecommerce.modules.customer.service.CustomerService;
import com.riannegreiros.springecommerce.modules.customer.token.entity.ConfirmationToken;
import com.riannegreiros.springecommerce.modules.customer.token.service.Impl.TokenServiceImpl;
import com.riannegreiros.springecommerce.utils.JWTConstants;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.Date;
import java.util.UUID;

@Service
@Transactional
public class CustomerServiceImpl implements CustomerService {

    private final CustomerRepository customerRepository;
    private final PasswordEncoder passwordEncoder;
    private final TokenServiceImpl tokenService;

    public CustomerServiceImpl(CustomerRepository customerRepository, PasswordEncoder passwordEncoder, TokenServiceImpl tokenService) {
        this.customerRepository = customerRepository;
        this.passwordEncoder = passwordEncoder;
        this.tokenService = tokenService;
    }

    @Override
    public Customer findByEmail(String email) {
        return customerRepository.findByEmail(email).orElseThrow(() -> new ResourceNotFoundException("User", "email", email));
    }

    @Override
    public Customer save(Customer customer) {
        customerRepository.findByEmail(customer.getEmail()).orElseThrow(() -> new Error("Customer already exists with this email:" + customer.getEmail()));
        customer.setPassword(passwordEncoder.encode(customer.getPassword()));
        Customer savedCustomer =  customerRepository.save(customer);

        String token = JWT.create().sign(Algorithm.HMAC512(JWTConstants.JWT_SECRET.getBytes()));
        ConfirmationToken confirmationToken = new ConfirmationToken(
                token,
                new Date(),
                new Date(System.currentTimeMillis() + JWTConstants.TOKEN_EXPIRATION),
                savedCustomer
        );
        String link = "http://localhost:8080/api/v1/registration/confirm?token=" + token;
        tokenService.save(confirmationToken);

        return savedCustomer;
    }

    @Override
    public void enable(UUID id) {
        Customer findCustomer = customerRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Customer", "ID", id.toString()));
        findCustomer.setEnabled(true);
    }
}
