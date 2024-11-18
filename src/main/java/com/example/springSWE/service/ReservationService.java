package com.example.springSWE.service;

import com.example.springSWE.model.Reservation;
import com.example.springSWE.model.User;
import com.example.springSWE.dao.ReservationRepository;

import java.util.List;
import java.time.LocalDate;
import java.time.LocalTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.Comparator;

@Service
public class ReservationService {
	@Autowired
	private ReservationRepository reservationRepository;

	public void saveReservation(Reservation reservation) {
		reservationRepository.save(reservation);
	}	

	public List<Reservation> getFutureReservations(Long parkID) {
        LocalDate today = LocalDate.now();
        LocalTime now = LocalTime.now();

        List<Reservation> reservations = reservationRepository.findByParkId(parkID)
        .stream()
        .filter(reservation -> 
            reservation.getDate().isAfter(today) || 
            (reservation.getDate().isEqual(today) && reservation.getStartTime().isAfter(now))
        )
        .sorted(Comparator.comparing(Reservation::getDate).thenComparing(Reservation::getStartTime))
        .collect(Collectors.toList());
        return reservations;
    }

    public boolean hasConcurrentReservation(User user, LocalDate date, LocalTime startTime, int durationInMinutes) {
        List<Reservation> userReservations = reservationRepository.findByUserAndDate(user, date);
        for (Reservation existingReservation : userReservations) {
            LocalTime existingStartTime = existingReservation.getStartTime();
            LocalTime existingEndTime = existingReservation.getEndTime();
            LocalTime newEndTime = startTime.plusMinutes(durationInMinutes);
            if ((startTime.isBefore(existingEndTime) && newEndTime.isAfter(existingStartTime)) ||
                startTime.equals(existingStartTime)) {
                return true;
            }
        }
        return false;
    }


	public List<Reservation> getReservationsByParkAndDate(Long parkID, LocalDate date) {
		return reservationRepository.findByParkIdAndDate(parkID, date);
    }

	public List<Reservation> getAllReservationsByPark(Long parkID) {
        List<Reservation> reservations = reservationRepository.findByParkId(parkID);
        reservations.sort(Comparator.comparing(Reservation::getDate).thenComparing(Reservation::getStartTime));
		return reservations;
	}

    public List<Reservation> getFutureReservationsByUser(User user) {
        LocalDate today = LocalDate.now();
        LocalTime now = LocalTime.now();

        List<Reservation> reservations = reservationRepository.findByUserId(user.getId())
        .stream()
        .filter(reservation -> 
            reservation.getDate().isAfter(today) || 
            (reservation.getDate().isEqual(today) && reservation.getStartTime().isAfter(now))
        )
        .sorted(Comparator.comparing(Reservation::getDate).thenComparing(Reservation::getStartTime))
        .collect(Collectors.toList());
        return reservations;
    }

    public Optional<Reservation> getOngoingReservation(Long parkID, LocalDate date, LocalTime startTime) {
        List<Reservation> reservations = reservationRepository.findOngoingReservationByParkDateTime(parkID, date, startTime);
        return reservations.isEmpty() ? Optional.empty() : Optional.of(reservations.get(0));
    }
    
    public void deleteReservation(Long reservationID) {
        reservationRepository.deleteById(reservationID);
    }

    public List<Reservation> findReservationsForParkAndDateWithinTimeRange(Long parkId, LocalDate date, LocalTime startTime, LocalTime endTime) {
        return reservationRepository.findByParkIdAndDateAndStartTimeLessThanAndEndTimeGreaterThan(
                parkId, date, endTime, startTime);
    }
}
