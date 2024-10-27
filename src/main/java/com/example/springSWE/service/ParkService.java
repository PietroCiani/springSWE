package com.example.springSWE.service;

import com.example.springSWE.model.Park;
import com.example.springSWE.dao.ParkRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class ParkService {
	@Autowired
    private ParkRepository parkRepository;
    
    public void savePark(Park park) {	
		parkRepository.save(park);
    }

    public boolean checkParkExists(String address) {
		return parkRepository.findByAddress(address).isPresent();
    }

	public List<Park> findAllParks() {
		return parkRepository.findAll();
	}

	public Park findParkById(Long id) {
		return parkRepository.findById(id).orElse(null);
	}
}
