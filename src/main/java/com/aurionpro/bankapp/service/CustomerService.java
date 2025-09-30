package com.aurionpro.bankapp.service;

import java.util.List;
import java.util.Map;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import com.aurionpro.bankapp.entity.Customer;

public interface CustomerService {

    // Create a new customer
    Customer create(Customer customer);

    // Get customer by ID
    Customer getById(Long id);

    // Get all customers
    List<Customer> getAll();

    // Full update of customer
    Customer update(Long id, Customer customer);

    // Partial update of customer fields (PATCH)
    Customer updatePartial(Long id, Map<String, Object> updates);

    // Save customer (useful for cascading save)
    Customer save(Customer customer);

    // Delete customer by ID
    void delete(Long id);
    
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException;
}
