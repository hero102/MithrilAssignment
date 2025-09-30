package com.aurionpro.bankapp.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class RegisterRequest {

    @NotBlank
    private String username;

    @NotBlank
    private String password;

    private String email;
    private String name;
	private String contactNo;
    private String dob;      // yyyy-MM-dd
    private String city;
    private String state;
    private String pincode;
}
