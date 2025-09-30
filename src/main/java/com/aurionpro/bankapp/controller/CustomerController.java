package com.aurionpro.bankapp.controller;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.aurionpro.bankapp.dto.CustomerResponse;
import com.aurionpro.bankapp.dto.EntityMapper;
import com.aurionpro.bankapp.dto.RegisterRequest;
import com.aurionpro.bankapp.entity.Address;
import com.aurionpro.bankapp.entity.Customer;
import com.aurionpro.bankapp.entity.Role;
import com.aurionpro.bankapp.entity.User;
import com.aurionpro.bankapp.repository.RoleRepository;
import com.aurionpro.bankapp.repository.UserRepository;
import com.aurionpro.bankapp.service.CustomerService;
import com.aurionpro.bankapp.service.EmailService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/customers")
public class CustomerController {

    private static final Logger log = LoggerFactory.getLogger(CustomerController.class);

    private final CustomerService customerService;
    private final RoleRepository roleRepo;
    private final UserRepository userRepo;
    private final PasswordEncoder passwordEncoder;
    private final EmailService emailService;

    public CustomerController(CustomerService customerService, RoleRepository roleRepo,
                              UserRepository userRepo, PasswordEncoder passwordEncoder, EmailService emailService) {
        this.customerService = customerService;
        this.roleRepo = roleRepo;
        this.userRepo = userRepo;
        this.passwordEncoder = passwordEncoder;
        this.emailService = emailService;
    }

    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<CustomerResponse> createCustomer(@Valid @RequestBody RegisterRequest req) {

        String username = req.getUsername().trim();
        String email = req.getEmail().trim();
        String name = req.getName().trim();
        String rawPassword = req.getPassword();
        String contactNo = req.getContactNo().trim();
        String dobStr = req.getDob().trim();
        String city = req.getCity().trim();
        String state = req.getState().trim();
        String pincode = req.getPincode().trim();

        // Required fields validation
        if (username.isEmpty() || rawPassword.isEmpty() || email.isEmpty() || name.isEmpty()) {
            return ResponseEntity.badRequest().body(null);
        }

        // Check duplicate username/email
        if (userRepo.findByUsername(username).isPresent()) {
            return ResponseEntity.badRequest().body(null);
        }


        // Create User
        User user = new User();
        user.setUsername(username);
        user.setEmail(email);
        user.setPassword(rawPassword); // plain, service will encode

        Role customerRole = roleRepo.findByName("ROLE_CUSTOMER")
                .orElseThrow(() -> new RuntimeException("ROLE_CUSTOMER not found"));
        user.setRoles(Set.of(customerRole));

        // Create Address & Customer
        Address address = new Address(city, state, pincode);
        Customer customer = new Customer();
        customer.setName(name);
        customer.setContactNo(contactNo);
        customer.setDob(LocalDate.parse(dobStr));
        customer.setUser(user);
        customer.setAddress(address);

        // Save Customer (password encoded inside service)
        Customer savedCustomer = customerService.create(customer);

        // Send welcome email
        String subject = "Welcome to Our Bank";
        String emailBody = String.format(
                "Dear %s,\n\nYour customer account has been created successfully!\n\nUsername: %s\nPassword: %s\n\nPlease change your password after first login.\n\nThank you for banking with us.",
                name, username, rawPassword
        );
        emailService.sendSimpleMessage(email, subject, emailBody);

        // Map to response WITHOUT ID
        CustomerResponse response = EntityMapper.toCustomerResponse(savedCustomer);

        return ResponseEntity.ok(response);
    }
    // =================== GET ALL CUSTOMERS (ADMIN ONLY) ===================
    @GetMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<CustomerResponse>> getAllCustomers() {
        List<Customer> customers = customerService.getAll();
        List<CustomerResponse> responses = customers.stream()
                .map(EntityMapper::toCustomerResponse)
                .toList();
        return ResponseEntity.ok(responses);
    }

    // =================== GET CUSTOMER BY ID ===================
    @GetMapping("/{id}")
    public ResponseEntity<CustomerResponse> getCustomerById(@PathVariable Long id) {
        Customer customer = customerService.getById(id);

        boolean isAdmin = SecurityContextHolder.getContext().getAuthentication().getAuthorities()
                .stream().anyMatch(a -> a.getAuthority().equals("ROLE_ADMIN"));

        if (!isAdmin) {
            String username = SecurityContextHolder.getContext().getAuthentication().getName();
            if (!customer.getUser().getUsername().equals(username)) {
                return ResponseEntity.status(403).build();
            }
        }

        CustomerResponse response = EntityMapper.toCustomerResponse(customer);

        return ResponseEntity.ok(response);
    }

    // =================== PATCH: CUSTOMER PROFILE / PASSWORD UPDATE ===================
    @PatchMapping("/{id}")
    public ResponseEntity<CustomerResponse> updateCustomerProfile(
            @PathVariable Long id,
            @RequestBody Map<String, Object> updates) {

        Customer customer = customerService.getById(id);

        boolean isAdmin = SecurityContextHolder.getContext().getAuthentication().getAuthorities()
                .stream().anyMatch(a -> a.getAuthority().equals("ROLE_ADMIN"));

        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        if (!isAdmin && !customer.getUser().getUsername().equals(username)) {
            return ResponseEntity.status(403).build();
        }

        Customer updatedCustomer = customerService.updatePartial(id, updates);

        CustomerResponse response = EntityMapper.toCustomerResponse(updatedCustomer);
        return ResponseEntity.ok(response);
    }
}
