package com.example.springSWE.model;

import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.time.LocalTime;

import static org.junit.jupiter.api.Assertions.*;

public class ReservationTest {

    @Test
    public void testReservationObjectCreation() {
        LocalDate date = LocalDate.of(2024, 12, 15);
        LocalTime startTime = LocalTime.of(10, 0);
        int duration = 60;
        User user = new User();
        Park park = new Park();

        Reservation reservation = new Reservation(date, startTime, duration, user, park);

        assertEquals(date, reservation.getDate());
        assertEquals(startTime, reservation.getStartTime());
        assertEquals(startTime.plusMinutes(duration), reservation.getEndTime());
        assertEquals(duration, reservation.getDuration());
        assertEquals(user, reservation.getUser());
        assertEquals(park, reservation.getPark());
    }

    @Test
    public void testSetStartTimeUpdatesEndTime() {
        LocalDate date = LocalDate.of(2024, 12, 15);
        LocalTime startTime = LocalTime.of(10, 0);
        int duration = 60;

        Reservation reservation = new Reservation(date, startTime, duration, new User(), new Park());
        reservation.setStartTime(LocalTime.of(11, 0));

        assertEquals(LocalTime.of(11, 0), reservation.getStartTime());
        assertEquals(LocalTime.of(12, 0), reservation.getEndTime());
    }
}
