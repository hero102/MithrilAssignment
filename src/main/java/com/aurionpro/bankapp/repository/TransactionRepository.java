package com.aurionpro.bankapp.repository;

import com.aurionpro.bankapp.entity.Transaction;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface TransactionRepository extends JpaRepository<Transaction, Long> {

    List<Transaction> findByAccount_Id(Long accountId);

    List<Transaction> findByCustomer_Id(Long customerId);
}
