package com.example.springSWE.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;

import com.example.springSWE.model.Reservation;

import java.util.List;
import java.time.LocalDate;
import java.time.LocalTime;

public interface ReservationRepository extends JpaRepository<Reservation, Long> {
    List<Reservation> findByParkId(Long parkId);
    List<Reservation> findByParkIdAndDate(Long parkId, LocalDate date);
    List<Reservation> findByParkIdAndDateAndTimeAfter(
        @Param("parkId") Long parkId,
        @Param("date") LocalDate date,
        @Param("time") LocalTime time
    );
}
