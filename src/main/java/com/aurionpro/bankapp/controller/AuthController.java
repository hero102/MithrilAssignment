package com.aurionpro.bankapp.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.aurionpro.bankapp.dto.LoginRequest;
import com.aurionpro.bankapp.dto.RegisterRequest;
import com.aurionpro.bankapp.dto.JwtResponse;
import com.aurionpro.bankapp.entity.Customer;
import com.aurionpro.bankapp.service.AuthService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    // ------------------ Register Customer ------------------
    @PostMapping("/register")
    public ResponseEntity<?> register(@Valid @RequestBody RegisterRequest req) {

        Customer saved = authService.registerCustomer(
                req.getUsername(),
                req.getPassword(), 
                req.getEmail(),
                req.getName(),
                req.getContactNo(),
                req.getDob(),
                req.getCity(),
                req.getState(),
                req.getPincode()
        );

        return ResponseEntity.ok("Customer registered ");
    }

    // ------------------ Login ------------------
    @PostMapping("/login")
    public ResponseEntity<JwtResponse> login(@Valid @RequestBody LoginRequest req) {

        String token = authService.login(req.getUsername(), req.getPassword());

        return ResponseEntity.ok(new JwtResponse(token));
    }
}
