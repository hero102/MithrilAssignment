package com.aurionpro.bankapp.dto;

import java.time.LocalDate;

import lombok.Data;

@Data
public class CustomerResponse {
    private String username;
    private String name;
    private String email;
    private String contactNo;
    private LocalDate dob;
    private String city;
    private String state;
    private String pincode;

    public CustomerResponse() {}

    public CustomerResponse(com.aurionpro.bankapp.entity.Customer customer) {
        this.name = customer.getName();
        this.username = customer.getUser().getUsername();
        this.email = customer.getUser().getEmail();
        this.contactNo = customer.getContactNo();
        this.dob = customer.getDob();
        if (customer.getAddress() != null) {
            this.city = customer.getAddress().getCity();
            this.state = customer.getAddress().getState();
            this.pincode = customer.getAddress().getPincode();
        }
    }
}
