package com.example.springSWE.controller;

import com.example.springSWE.model.Park;
import com.example.springSWE.model.Reservation;
import com.example.springSWE.model.User;
import com.example.springSWE.service.ParkService;
import com.example.springSWE.service.ReservationService;
import com.example.springSWE.service.UserService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.ui.Model;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.LocalDateTime;
import java.util.List;
import java.security.Principal;


@Controller
public class ReservationController {

	@Autowired
	private ReservationService reservationService;

	@Autowired
	private ParkService parkService;

	@Autowired
	private UserService userService;

	@PostMapping("/reservation/create")
		public String createReservation(@RequestParam("parkId") Long parkId,
                               @RequestParam("time") @DateTimeFormat(pattern = "HH:mm") LocalTime time,
                               @RequestParam("duration") int durationInMinutes,
                               Principal principal) {
		// Fetch the park based on the parkId
		Park park = parkService.findParkById(parkId);
		
		User user = userService.getUserByUsername(principal.getName());
		
		// Create a new reservation
		Reservation reservation = new Reservation();
		reservation.setPark(park);
		reservation.setUser(user);
		reservation.setTime(time);
		reservation.setDuration(durationInMinutes);
		
		// Save the reservation
		reservationService.saveReservation(reservation);
		
		// Redirect the user to the parks page or the reservation page
		return "redirect:/parks";
	}

	
}
