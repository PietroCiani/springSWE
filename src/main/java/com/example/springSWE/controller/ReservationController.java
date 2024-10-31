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
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import java.time.LocalDate;
import java.time.LocalTime;
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
							@RequestParam("date") @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate date,
							@RequestParam("startTime") @DateTimeFormat(pattern = "HH:mm") LocalTime startTime,
							@RequestParam("duration") int durationInMinutes,
							Principal principal,
							RedirectAttributes redirectAttributes) {
		try {
			Park park = parkService.findParkById(parkId);
			User user = userService.getUserByUsername(principal.getName());
			Reservation reservation = new Reservation(date, startTime, durationInMinutes, user, park);

			// TODO: Add validation logic here to check for overlapping reservations
			
			reservationService.saveReservation(reservation);
			redirectAttributes.addFlashAttribute("success", "Reservation successfully created!");
			return "redirect:/parks";

		} catch (Exception e) {
			redirectAttributes.addFlashAttribute("error", 
            "Could not create reservation: " + e.getMessage());
        	return "redirect:/reservation/schedule?parkId=" + parkId;
		}
	}

	@PostMapping("/reservation/delete")
	public String deleteReservation(@RequestParam("reservationId") Long reservationId,
									@RequestParam("parkId") Long parkId,
									RedirectAttributes redirectAttributes) {
		try {
			reservationService.deleteReservation(reservationId);
			redirectAttributes.addFlashAttribute("success", "Reservation successfully deleted!");
			return "redirect:/schedule?parkId=" + parkId;
		} catch (Exception e) {
			redirectAttributes.addFlashAttribute("error", 
			"Could not delete reservation: " + e.getMessage());
			return "redirect:/schedule?parkId=" + parkId;
		}
	}

}
