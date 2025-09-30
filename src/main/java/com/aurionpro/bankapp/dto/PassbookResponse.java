package com.aurionpro.bankapp.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import java.math.BigDecimal;
import java.util.List;

@Data
@AllArgsConstructor
public class PassbookResponse {
    private BigDecimal currentBalance;
    private List<TransactionResponse> transactions;
}
