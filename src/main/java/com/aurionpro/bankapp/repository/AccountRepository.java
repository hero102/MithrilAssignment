package com.aurionpro.bankapp.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.aurionpro.bankapp.entity.Account;

import java.util.List;
import java.util.Optional;

public interface AccountRepository extends JpaRepository<Account, Long> {
    Optional<Account> findByAccountNumber(String accountNumber);
    List<Account> findByCustomer_Id(Long customerId);
}

