package com.aurionpro.bankapp.service.impl;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.aurionpro.bankapp.dto.AccountRequest;
import com.aurionpro.bankapp.entity.Account;
import com.aurionpro.bankapp.entity.Customer;
import com.aurionpro.bankapp.exception.ResourceNotFoundException;
import com.aurionpro.bankapp.repository.AccountRepository;
import com.aurionpro.bankapp.repository.CustomerRepository;
import com.aurionpro.bankapp.service.AccountService;

@Service
@Transactional
public class AccountServiceImpl implements AccountService {

    private final AccountRepository accountRepo;
    private final CustomerRepository customerRepo;

    public AccountServiceImpl(AccountRepository accountRepo, CustomerRepository customerRepo) {
        this.accountRepo = accountRepo;
        this.customerRepo = customerRepo;
    }

    @Override
    public Account create(AccountRequest req) {
        Customer customer = customerRepo.findById(req.getCustomerId())
                .orElseThrow(() -> new ResourceNotFoundException("Customer not found: " + req.getCustomerId()));

        BigDecimal initialDeposit = req.getInitialDeposit();
        if (initialDeposit == null) {
            throw new IllegalArgumentException("Initial deposit is required");
        }
        if (initialDeposit.compareTo(BigDecimal.ZERO) < 0) {
            throw new IllegalArgumentException("Initial deposit cannot be negative");
        }

        Account account = new Account();
        account.setAccountNumber(generateAccountNumber());
        account.setAccountType(req.getAccountType());
        account.setBalance(initialDeposit); 
        account.setActive(true);
        account.setCustomer(customer);

        return accountRepo.save(account);
    }


    private String generateAccountNumber() {
        return UUID.randomUUID().toString().replaceAll("-", "").substring(0, 12).toUpperCase();
    }

    @Override
    public Account getById(Long id) {
        return accountRepo.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Account not found: " + id));
    }

    @Override
    public List<Account> getAll() {
        return accountRepo.findAll();
    }

    @Override
    public List<Account> getByCustomerId(Long customerId) {
        // validates customer exists before fetching accounts
        if (!customerRepo.existsById(customerId)) {
            throw new ResourceNotFoundException("Customer not found: " + customerId);
        }
        return accountRepo.findByCustomer_Id(customerId);
    }
    
    @Override
    public Account activate(Long id) {
        Account account = getById(id);

        if (account.isActive()) {
            throw new IllegalStateException("Account already active: " + id);
        }

        account.setActive(true);
        return accountRepo.save(account);
    }


    @Override
    public Account deactivate(Long id) {
        Account account = getById(id);

        if (!account.isActive()) {
            throw new IllegalStateException("Account already deactivated: " + id);
        }

        account.setActive(false);
        return accountRepo.save(account);
    }
}
