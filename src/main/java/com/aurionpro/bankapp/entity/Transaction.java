package com.aurionpro.bankapp.entity;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import jakarta.persistence.*;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import com.aurionpro.bankapp.dto.TransactionResponse;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@JsonIgnoreProperties({"hibernateLazyInitializer","handler"})
@Entity
@Table(name = "transactions")
public class Transaction {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "Transaction type is required")
    private String transtype;

    @DecimalMin(value = "1.0", message = "Amount must be greater than 0")
    private BigDecimal amount;

    @NotNull(message = "Transaction date is required")
    private LocalDateTime dateTime;

    private String remarks;

    // Keep both sender and receiver so service/pdf/mapping can use them
    @Column(name = "sender_name")
    private String senderName;

    @Column(name = "receiver_name")
    private String receiverName;

    @ManyToOne
    @JoinColumn(name = "account_id")
    private Account account;

    @ManyToOne
    @JoinColumn(name = "customer_id")
    private Customer customer;

    // Convert to DTO
    public TransactionResponse toTransactionResponse() {
        TransactionResponse resp = new TransactionResponse();
        resp.setId(this.id);
        resp.setTranstype(this.transtype);
        resp.setAmount(this.amount);
        resp.setDateTime(this.dateTime);
        resp.setRemarks(this.remarks);
        resp.setSenderName(this.senderName);
        resp.setReceiverName(this.receiverName);
        return resp;
    }
}
