package com.aurionpro.bankapp.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.RequiredArgsConstructor;

@Entity
@Data
@AllArgsConstructor
@RequiredArgsConstructor
@Table(name = "addresses")
public class Address {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "City is required")
    private String city;

    @NotBlank(message = "State is required")
    private String state;

    @NotBlank(message = "Pincode is required")
    @Pattern(regexp = "\\d{6}", message = "Pincode must be 6 digits")
    private String pincode;

	public Address(@NotBlank(message = "City is required") String city,
			@NotBlank(message = "State is required") String state,
			@NotBlank(message = "Pincode is required") @Pattern(regexp = "\\d{6}", message = "Pincode must be 6 digits") String pincode) {
		super();
		this.city = city;
		this.state = state;
		this.pincode = pincode;
	}
    
    
}
