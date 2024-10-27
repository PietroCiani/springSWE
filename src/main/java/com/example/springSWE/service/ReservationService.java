package com.example.springSWE.service;

import com.example.springSWE.model.Reservation;
import com.example.springSWE.dao.ReservationRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ReservationService {
	@Autowired
	private ReservationRepository reservationRepository;

	//TODO: saveReservation(Reservation reservation)

	
}
