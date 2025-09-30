package com.aurionpro.bankapp.service;

import com.aurionpro.bankapp.dto.AccountRequest;
import com.aurionpro.bankapp.entity.Account;

import java.util.List;

public interface AccountService {
    Account create(AccountRequest req);
    Account getById(Long id);
    List<Account> getAll();
    List<Account> getByCustomerId(Long customerId);
    Account deactivate(Long id);
    public Account activate(Long id); 
}
