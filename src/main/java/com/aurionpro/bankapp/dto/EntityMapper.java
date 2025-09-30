package com.aurionpro.bankapp.dto;

import com.aurionpro.bankapp.entity.Account;
import com.aurionpro.bankapp.entity.Customer;
import com.aurionpro.bankapp.entity.Transaction;

public class EntityMapper {

    // ------------------ Customer → CustomerResponse ------------------
    public static CustomerResponse toCustomerResponse(Customer c) {
        if (c == null) return null;

        String username = null;
        String email = null;
        if (c.getUser() != null) {
            username = c.getUser().getUsername();
            email = c.getUser().getEmail();
        }

        String city = null, state = null, pincode = null;
        if (c.getAddress() != null) {
            city = c.getAddress().getCity();
            state = c.getAddress().getState();
            pincode = c.getAddress().getPincode();
        }

        CustomerResponse response = new CustomerResponse();
        response.setUsername(username);
        response.setEmail(email);
        response.setName(c.getName());
        response.setContactNo(c.getContactNo());
        response.setDob(c.getDob());
        response.setCity(city);
        response.setState(state);
        response.setPincode(pincode);

        return response;
    }

    // ------------------ Transaction → TransactionResponse ------------------
    public static TransactionResponse toTransactionResponse(Transaction t, String loggedInCustomerName) {
        if (t == null) return null;

        String senderName = t.getSenderName() != null ? t.getSenderName() : "Unknown";

        String remarks;
        if (senderName.equals(loggedInCustomerName)) {
            remarks = "You initiated: " + (t.getRemarks() != null ? t.getRemarks() : "");
        } else {
            remarks = "From: " + senderName;
        }

        TransactionResponse resp = new TransactionResponse();
        resp.setId(t.getId());
        resp.setTranstype(t.getTranstype());
        resp.setAmount(t.getAmount());
        resp.setDateTime(t.getDateTime());
        resp.setRemarks(remarks);
        resp.setSenderName(senderName);

        return resp;
    }

    // ------------------ Account → AccountResponse ------------------
    public static AccountResponse toAccountResponse(Account a, String loggedInCustomerName) {
        if (a == null) return null;

        Long custId = a.getCustomer() != null ? a.getCustomer().getId() : null;

        AccountResponse resp = new AccountResponse();
        resp.setAccountNumber(a.getAccountNumber());
        resp.setAccountType(a.getAccountType());
        resp.setBalance(a.getBalance());
        resp.setActive(a.isActive());
        resp.setCustomerId(custId);
        return resp;
    }

    // Simple version
    public static AccountResponse toAccountResponse(Account a) {
        String customerName = a.getCustomer() != null ? a.getCustomer().getName() : null;
        return toAccountResponse(a, customerName);
    }
}
