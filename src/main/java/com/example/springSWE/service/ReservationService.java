package com.example.springSWE.service;

import com.example.springSWE.model.Park;
import com.example.springSWE.model.Reservation;
import com.example.springSWE.dao.ReservationRepository;

import java.util.List;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Collections;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cglib.core.Local;
import org.springframework.stereotype.Service;

@Service
public class ReservationService {
	@Autowired
	private ReservationRepository reservationRepository;

	public void saveReservation(Reservation reservation) {
		reservationRepository.save(reservation);
	}	

	public List<Reservation> getFutureReservations(Long parkID, LocalDate date, LocalTime time) {
		return reservationRepository.findByParkIdAndDateAndTimeAfter(parkID, date, time);
		//FIXME: include ongoing reservation (started before LocalTime.now())
	}

	public List<Reservation> getReservationsByParkAndDate(Long parkId, LocalDate date) {
		return reservationRepository.findByParkIdAndDate(parkId, date);
    }

	public List<Reservation> getAllReservationsByPark(Long parkId) {
		return reservationRepository.findByParkId(parkId);
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
