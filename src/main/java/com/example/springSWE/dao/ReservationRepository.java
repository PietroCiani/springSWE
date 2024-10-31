package com.example.springSWE.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.example.springSWE.model.Reservation;

import java.util.List;
import java.time.LocalDate;
import java.time.LocalTime;

public interface ReservationRepository extends JpaRepository<Reservation, Long> {
    List<Reservation> findByParkId(Long parkId);
    List<Reservation> findByParkIdAndDate(Long parkId, LocalDate date);
    List<Reservation> findByParkIdAndDateAndStartTimeAfter(
        @Param("parkId") Long parkId,
        @Param("date") LocalDate date,
        @Param("startTime") LocalTime startTime
    );

    @Query("SELECT r FROM Reservation r WHERE r.park.id = :parkID AND r.date = :date AND :startTime BETWEEN r.startTime AND r.endTime")
    List<Reservation> findOngoingReservationByParkDateTime(@Param("parkID") Long parkID, @Param("date") LocalDate date, @Param("startTime") LocalTime startTime);
    
}
