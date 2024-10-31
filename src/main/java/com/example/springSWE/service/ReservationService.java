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
import java.util.Comparator;

@Service
public class ReservationService {
	@Autowired
	private ReservationRepository reservationRepository;

	public void saveReservation(Reservation reservation) {
		reservationRepository.save(reservation);
	}	

	public List<Reservation> getFutureReservations(Long parkID, LocalDate date, LocalTime startTime) {
        List<Reservation> reservations = reservationRepository.findByParkIdAndDateAndStartTimeAfter(parkID, date, startTime);
        
        Optional<Reservation> ongoing = getOngoingReservation(parkID, LocalDate.now(), LocalTime.now());
        ongoing.ifPresent(reservations::add);
        
        reservations.sort(Comparator.comparing(Reservation::getDate).thenComparing(Reservation::getStartTime));
        
        return reservations;
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
        return reservationRepository.findByUserAndDateAndStartTimeAfter(user, LocalDate.now(), LocalTime.now());
    }

    public Optional<Reservation> getOngoingReservation(Long parkID, LocalDate date, LocalTime startTime) {
        List<Reservation> reservations = reservationRepository.findOngoingReservationByParkDateTime(parkID, date, startTime);
        return reservations.isEmpty() ? Optional.empty() : Optional.of(reservations.get(0));
    }
    
    public void deleteReservation(Long reservationID) {
        reservationRepository.deleteById(reservationID);
    }


	/*
    public static List<int[]> findAvailableSlots(List<int[]> transReservations, int maxTime) {
        // sort reservations by start time
		Collections.sort(transReservations, (a, b) -> Integer.compare(a[0], b[0]));
		List<int[]> availableSlots = new ArrayList<>();
		// 0 -> first
        if (transReservations.isEmpty() || transReservations.get(0)[0] > 0) {
            availableSlots.add(new int[]{0, transReservations.isEmpty() ? maxTime : transReservations.get(0)[0]});
        }
		// between reservations
        for (int i = 0; i < transReservations.size(); i++) {
            int currentEnd = transReservations.get(i)[1];
            int nextStart = (i == transReservations.size() - 1) ? 
                maxTime : transReservations.get(i + 1)[0];
                
            if (currentEnd < nextStart) {
                availableSlots.add(new int[]{currentEnd, nextStart});
            }
        }
        // last -> maxTime
        if (!transReservations.isEmpty() && 
            transReservations.get(transReservations.size() - 1)[1] < maxTime) {
            availableSlots.add(new int[]{
                transReservations.get(transReservations.size() - 1)[1], 
                maxTime
            });
        }
        return availableSlots;
    }*/
}
