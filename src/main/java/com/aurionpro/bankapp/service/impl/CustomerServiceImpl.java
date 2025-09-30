package com.aurionpro.bankapp.service.impl;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.aurionpro.bankapp.entity.Address;
import com.aurionpro.bankapp.entity.Customer;
import com.aurionpro.bankapp.entity.User;
import com.aurionpro.bankapp.exception.ResourceNotFoundException;
import com.aurionpro.bankapp.repository.CustomerRepository;
import com.aurionpro.bankapp.repository.UserRepository;
import com.aurionpro.bankapp.service.CustomerService;

@Service
public class CustomerServiceImpl implements CustomerService, UserDetailsService {

    private final CustomerRepository customerRepo;
    private final UserRepository userRepo;
    private final PasswordEncoder passwordEncoder;

    public CustomerServiceImpl(CustomerRepository customerRepo, UserRepository userRepo, PasswordEncoder passwordEncoder) {
        this.customerRepo = customerRepo;
        this.userRepo = userRepo;
        this.passwordEncoder = passwordEncoder;
    }

    // ---------------- Create Customer ----------------
    @Override
    public Customer create(Customer customer) {
        if (customer.getUser() != null && customer.getUser().getPassword() != null) {
            customer.getUser().setPassword(passwordEncoder.encode(customer.getUser().getPassword()));
        }
        return customerRepo.save(customer);
    }

    @Override
    public Customer getById(Long id) {
        return customerRepo.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Customer not found: " + id));
    }

    @Override
    public List<Customer> getAll() {
        return customerRepo.findAll();
    }

    @Override
    public Customer update(Long id, Customer customer) {
        Customer existing = getById(id);

        // Customer personal info
        existing.setName(customer.getName());
        existing.setContactNo(customer.getContactNo());
        existing.setDob(customer.getDob());

        // Address
        if (customer.getAddress() != null) {
            if (existing.getAddress() == null) existing.setAddress(new Address());
            existing.getAddress().setCity(customer.getAddress().getCity());
            existing.getAddress().setState(customer.getAddress().getState());
            existing.getAddress().setPincode(customer.getAddress().getPincode());
        }

        // User info (email/password)
        if (customer.getUser() != null) {
            User user = existing.getUser();
            if (user == null) {
                user = new User();
                existing.setUser(user);
            }
            if (customer.getUser().getEmail() != null) user.setEmail(customer.getUser().getEmail());
            if (customer.getUser().getPassword() != null)
                user.setPassword(passwordEncoder.encode(customer.getUser().getPassword()));
        }

        return customerRepo.save(existing);
    }

    @Override
    @Transactional
    public Customer updatePartial(Long id, Map<String, Object> updates) {
        Customer customer = getById(id);

        updates.forEach((key, value) -> {
            switch (key) {
                case "name": customer.setName((String) value); break;
                case "contactNo": customer.setContactNo((String) value); break;
                case "dob": customer.setDob(LocalDate.parse((String) value)); break;
                case "password":
                    if (customer.getUser() != null) {
                        customer.getUser().setPassword(passwordEncoder.encode((String) value));
                    }
                    break;
                case "email":
                    if (customer.getUser() != null) {
                        customer.getUser().setEmail((String) value);
                    }
                    break;
                case "address":
                    @SuppressWarnings("unchecked")
                    Map<String, Object> addrMap = (Map<String, Object>) value;
                    Address address = customer.getAddress();
                    if (address == null) { address = new Address(); customer.setAddress(address); }
                    if (addrMap.containsKey("city")) address.setCity((String) addrMap.get("city"));
                    if (addrMap.containsKey("state")) address.setState((String) addrMap.get("state"));
                    if (addrMap.containsKey("pincode")) address.setPincode((String) addrMap.get("pincode"));
                    break;
                default: throw new IllegalArgumentException("Invalid field: " + key);
            }
        });

        return customerRepo.save(customer);
    }

    @Override
    public Customer save(Customer customer) {
        return customerRepo.save(customer);
    }

    // ---------------- UserDetailsService ----------------
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        // Admin check first
        Optional<User> userOpt = userRepo.findByUsername(username);
        if (userOpt.isPresent()) {
            User user = userOpt.get();
            return new org.springframework.security.core.userdetails.User(
                    user.getUsername(),
                    user.getPassword(),
                    user.getRoles().stream().map(r -> new SimpleGrantedAuthority(r.getName())).collect(Collectors.toList())
            );
        }

        // Customer via User entity
        Optional<Customer> custOpt = customerRepo.findByUserUsername(username);
        if (custOpt.isPresent()) {
            Customer customer = custOpt.get();
            User user = customer.getUser();
            return new org.springframework.security.core.userdetails.User(
                    user.getUsername(),
                    user.getPassword(),
                    user.getRoles().stream().map(r -> new SimpleGrantedAuthority(r.getName())).collect(Collectors.toList())
            );
        }

        throw new UsernameNotFoundException("User not found: " + username);
    }

    @Override
    public void delete(Long id) {
        customerRepo.deleteById(id);
    }
}
