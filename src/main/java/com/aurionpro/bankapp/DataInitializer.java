package com.aurionpro.bankapp;

import java.util.Set;

import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import com.aurionpro.bankapp.entity.Role;
import com.aurionpro.bankapp.entity.User;
import com.aurionpro.bankapp.repository.RoleRepository;
import com.aurionpro.bankapp.repository.UserRepository;

@Component
public class DataInitializer implements CommandLineRunner {

    private final RoleRepository roleRepo;
    private final UserRepository userRepo;
    private final PasswordEncoder passwordEncoder;

    public DataInitializer(RoleRepository roleRepo, UserRepository userRepo, PasswordEncoder passwordEncoder) {
        this.roleRepo = roleRepo;
        this.userRepo = userRepo;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public void run(String... args) throws Exception {
        // Ensure roles exist
        Role adminRole = roleRepo.findByName("ROLE_ADMIN")
                .orElseGet(() -> roleRepo.save(new Role("ROLE_ADMIN")));

        Role customerRole = roleRepo.findByName("ROLE_CUSTOMER")
                .orElseGet(() -> roleRepo.save(new Role("ROLE_CUSTOMER")));

        // Default admin user
        if (!userRepo.existsByUsername("adminuser")) {
            User admin = new User();
            admin.setUsername("adminuser");
            admin.setPassword(passwordEncoder.encode("admin123"));
            admin.setRoles(Set.of(adminRole));
            userRepo.save(admin);
            System.out.println("✅ Default admin user created: adminuser / admin123");
        }

    }
}
