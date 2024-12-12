package com.example.springSWE.controller;

import com.example.springSWE.model.Park;
import com.example.springSWE.model.Reservation;
import com.example.springSWE.model.User;
import com.example.springSWE.service.ParkService;
import com.example.springSWE.service.ReservationService;
import com.example.springSWE.service.UserService;

import jakarta.servlet.http.HttpServletRequest;

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
									Principal principal, RedirectAttributes redirectAttributes,
									HttpServletRequest request) {

		String referer = request.getHeader("Referer");
		try {
			// Preparare gli oggetti principali
			User user = userService.getUserByUsername(principal.getName());
			Park park = parkService.findParkById(parkId);
			LocalDateTime reservationDateTime = LocalDateTime.of(date, startTime);
			LocalTime endTime = startTime.plusMinutes(durationInMinutes);

			// Eseguire le verifiche
			String validationError = validateReservation(parkId, date, startTime, endTime, park, user,
			reservationDateTime, durationInMinutes);
			if (validationError != null) {
				redirectAttributes.addFlashAttribute("error", validationError);
				return "redirect:" + (referer != null ? referer : "/");
			}

			// Creare e salvare la prenotazione
			Reservation reservation = new Reservation(date, startTime, durationInMinutes, user, park);
			reservationService.saveReservation(reservation);
			redirectAttributes.addFlashAttribute("success", "Reservation successfully created!");
			return "redirect:/reservations";

		} catch (Exception e) {
			redirectAttributes.addFlashAttribute("error", "Could not create reservation: " + e.getMessage());
			return "redirect:" + (referer != null ? referer : "/");
		}
	}

	private String validateReservation(Long parkId, LocalDate date, LocalTime startTime, LocalTime endTime, 
                                   Park park, User user, LocalDateTime reservationDateTime, int durationInMinutes) {
		// Verifica per prenotazioni nel passato
		if (reservationDateTime.isBefore(LocalDateTime.now())) {
			return "Cannot book a reservation in the past.";
		}

		// Verifica per orari sovrapposti con altre prenotazioni nel parco
		List<Reservation> overlappingReservations = reservationService
				.findReservationsForParkAndDateWithinTimeRange(parkId, date, startTime, endTime);
		if (!overlappingReservations.isEmpty()) {
			return "Selected time is already booked.";
		}

		// Verifica per sovrapposizioni con altre prenotazioni dell'utente
		if (reservationService.hasConcurrentReservation(user, date, startTime, durationInMinutes)) {
			return "You already have a reservation at this time.";
		}

		// Verifica per orari di chiusura del parco
		if (!park.isOpen(startTime) || !park.isOpen(endTime)) {
			return "Park is closed at selected time.";
		}

		return null; // Nessun errore trovato
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
