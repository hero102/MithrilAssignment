package com.aurionpro.bank.model;

import java.math.BigDecimal;
import java.sql.Timestamp;

public class Transaction {
    private int id;
    private int fromId;
    private int toId;
    private BigDecimal amount;
    private Timestamp date;
    private String type;

    public Transaction(int id, int fromId, int toId, BigDecimal amount, Timestamp date, String type) {
        this.id = id;
        this.fromId = fromId;
        this.toId = toId;
        this.amount = amount;
        this.date = date;
        this.type = type;
    }

    // ✅ Getters
    public int getId() { return id; }
    public int getFromId() { return fromId; }
    public int getToId() { return toId; }
    public BigDecimal getAmount() { return amount; }
    public Timestamp getDate() { return date; }
    public String getType() { return type; }
}
