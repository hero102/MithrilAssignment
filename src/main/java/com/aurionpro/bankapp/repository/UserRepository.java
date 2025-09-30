package com.aurionpro.bankapp.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.aurionpro.bankapp.entity.User;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByUsername(String username);
    boolean existsByUsername(String username);
    Boolean existsByEmail(String email);
}
