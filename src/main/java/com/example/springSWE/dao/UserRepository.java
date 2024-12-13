package com.example.springSWE.dao;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.springSWE.model.User;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Integer> {
    Optional<User> findByUsername(String username);
    Optional<User> findByEmail(String email);
}
