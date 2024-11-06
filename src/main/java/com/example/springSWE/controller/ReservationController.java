package com.example.springSWE.controller;

import com.example.springSWE.model.Park;
import com.example.springSWE.model.Reservation;
import com.example.springSWE.model.User;
import com.example.springSWE.service.ParkService;
import com.example.springSWE.service.ReservationService;
import com.example.springSWE.service.UserService;

import org.springframework.ui.Model;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import java.time.LocalDate;
import java.time.LocalDateTime;
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

	@GetMapping("/reservations")
	public String showReservations(Principal principal, Model model) {
		User user = userService.getUserByUsername(principal.getName());
		List<Reservation> reservations = reservationService.getFutureReservationsByUser(user);
		List<Park> parks = parkService.findAllParks();
		model.addAttribute("reservations", reservations);
		model.addAttribute("parks", parks);

		return "reservations";
	}

	@PostMapping("/reservation/create")
	public String createReservation(@RequestParam("parkId") Long parkId,
							@RequestParam("date") @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate date,
							@RequestParam("startTime") @DateTimeFormat(pattern = "HH:mm") LocalTime startTime,
							@RequestParam("duration") int durationInMinutes,
							Principal principal,
							RedirectAttributes redirectAttributes) {
		try {
			// prevent reservations in the past
			LocalDateTime reservationDateTime = LocalDateTime.of(date, startTime);
			if (reservationDateTime.isBefore(LocalDateTime.now())) {
				redirectAttributes.addFlashAttribute("error", "Cannot book a reservation in the past.");
				return "redirect:/schedule?parkId=" + parkId;
			}

			Park park = parkService.findParkById(parkId);
			User user = userService.getUserByUsername(principal.getName());
			Reservation reservation = new Reservation(date, startTime, durationInMinutes, user, park);

			// check for overlapping reservations
			LocalTime endTime = startTime.plusMinutes(durationInMinutes);
			List<Reservation> overlappingReservations = reservationService
					.findReservationsForParkAndDateWithinTimeRange(parkId, date, startTime, endTime);
	
			if (!overlappingReservations.isEmpty()) {
				redirectAttributes.addFlashAttribute("error", "Selected time is already booked.");
				return "redirect:/schedule?parkId=" + parkId;
			}

			// prevent closed park reservations
			if (!park.isOpen(startTime) || !park.isOpen(endTime)) {
				redirectAttributes.addFlashAttribute("error", "Park is closed at selected time.");
				return "redirect:/schedule?parkId=" + parkId;
			}
			
			reservationService.saveReservation(reservation);
			redirectAttributes.addFlashAttribute("success", "Reservation successfully created!");
			return "redirect:/reservations";

		} catch (Exception e) {
			redirectAttributes.addFlashAttribute("error", 
            "Could not create reservation: " + e.getMessage());
        	return "redirect:/schedule?parkId=" + parkId;
		}
	}

	@PostMapping("/reservation/delete")
	public String deleteReservation(@RequestParam("reservationId") Long reservationId,
									RedirectAttributes redirectAttributes) {
		try {
			reservationService.deleteReservation(reservationId);
			redirectAttributes.addFlashAttribute("success", "Reservation successfully deleted!");
			return "redirect:/reservations";
		} catch (Exception e) {
			redirectAttributes.addFlashAttribute("error", 
			"Could not delete reservation: " + e.getMessage());
			return "redirect:/reservations";
		}
	}

}
