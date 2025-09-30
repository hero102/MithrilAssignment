package com.aurionpro.bankapp.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class TransferTransactionResponse {
    private TransactionResponse debitTransaction;
    private TransactionResponse creditTransaction;
}
