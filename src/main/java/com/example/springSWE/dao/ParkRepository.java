package com.example.springSWE.dao;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.springSWE.model.Park;

import java.util.Optional;

public interface ParkRepository extends JpaRepository <Park, Integer> {
	Optional<Park> findByAddress(String address);
	Optional<Park> findById(Long id);
}
