package com.example.springSWE.controller;

import com.example.springSWE.service.ReservationService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.ui.Model;

@Controller
@RequestMapping("/reservation")
public class ReservationController {

	@Autowired
	private ReservationService reservationService;

	@GetMapping
	public String viewParkSchedule(Model model) {
		//TODO: fetch all reservations with parkId and date
		return "park_schedule";
	}

}
