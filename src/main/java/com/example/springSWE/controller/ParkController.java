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
import java.time.LocalDate;

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
	public String viewParkSchedule(@RequestParam("parkId") Long parkId, Model model) {
        Park park = parkService.findParkById(parkId);

		// res. current day for this park
		List<Reservation> reservations = reservationService.getReservationsByParkAndDate(parkId, java.time.LocalDate.now());
		
		// Add the park and reservations to the model
		model.addAttribute("park", park);
		model.addAttribute("reservations", reservations);
    
    return "schedule";
    }

	//TODO: @PostMapping

}
