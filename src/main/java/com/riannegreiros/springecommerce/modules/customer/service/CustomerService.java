package com.riannegreiros.springecommerce.modules.customer.service;

import com.riannegreiros.springecommerce.modules.customer.entity.Customer;

import java.util.UUID;

public interface CustomerService {

    Customer findByEmail(String email);
    Customer save(Customer customer);
    void enable(UUID id);
}
