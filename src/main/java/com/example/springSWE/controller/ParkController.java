package com.example.springSWE.controller;

import com.example.springSWE.model.Park;
import com.example.springSWE.model.Reservation;
import com.example.springSWE.service.ParkService;
import com.example.springSWE.service.ReservationService;


import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.ui.Model;

@Controller
public class ParkController {
	
	@Autowired
    private ParkService parkService;

	@Autowired
	private ReservationService reservationService;

	@GetMapping("/parks")
	public String showParks(Model model) {
		List<Park> parks = parkService.findAllParks();
        model.addAttribute("parks", parks);
        return "parks";
	}

	@GetMapping("/schedule")
	public String getAllReservationsForPark(@RequestParam("parkId") Long parkId, Model model) {
		Park park = parkService.findParkById(parkId);

		List<Reservation> reservations = reservationService.getFutureReservations(parkId);

		model.addAttribute("park", park);
		model.addAttribute("reservations", reservations);
		return "schedule";
	}
}
