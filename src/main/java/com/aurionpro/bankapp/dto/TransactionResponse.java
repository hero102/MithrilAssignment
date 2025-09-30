package com.aurionpro.bankapp.dto;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import com.aurionpro.bankapp.entity.Transaction;

import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Data
public class TransactionResponse {
    private Long id;
    private String transtype;
    private BigDecimal amount;
    private LocalDateTime dateTime;
    private String remarks;

    private String senderName;
    private String receiverName;

    public TransactionResponse(Long id, String transtype, BigDecimal amount,
                               LocalDateTime dateTime, String remarks,
                               String senderName, String receiverName) {
        this.id = id;
        this.transtype = transtype;
        this.amount = amount;
        this.dateTime = dateTime;
        this.remarks = remarks;
        this.senderName = senderName;
        this.receiverName = receiverName;
    }

    // Construct from entity
    public TransactionResponse(Transaction t) {
        if (t == null) return;
        this.id = t.getId();
        this.transtype = t.getTranstype();
        this.amount = t.getAmount();
        this.dateTime = t.getDateTime();
        this.remarks = t.getRemarks();
        this.senderName = t.getSenderName();
        this.receiverName = t.getReceiverName();
    }
}
