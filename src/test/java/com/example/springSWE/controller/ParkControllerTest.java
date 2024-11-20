package com.example.springSWE.controller;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.ui.Model;

import com.example.springSWE.model.Park;
import com.example.springSWE.model.Reservation;
import com.example.springSWE.service.ParkService;
import com.example.springSWE.service.ReservationService;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class ParkControllerTest {

    @Mock
    private ParkService parkService;

    @Mock
    private ReservationService reservationService;

    @Mock
    private Model model;

    @InjectMocks
    private ParkController parkController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testShowParks() {
        List<Park> parks = Arrays.asList(new Park(), new Park());
        when(parkService.findAllParks()).thenReturn(parks);

        String viewName = parkController.showParks(model);

        assertEquals("parks", viewName);
        verify(parkService).findAllParks();
        verify(model).addAttribute("parks", parks);
    }

    @Test
    void testGetAllReservationsForPark() {
        Long parkId = 1L;
        Park park = new Park();
        List<Reservation> reservations = Arrays.asList(new Reservation(), new Reservation());
        
		// mockito when() used to mock the behavior of parkService.findParkById()
        when(parkService.findParkById(parkId)).thenReturn(park);
        when(reservationService.getFutureReservations(parkId)).thenReturn(reservations);

        String viewName = parkController.getAllReservationsForPark(parkId, model);

        assertEquals("schedule", viewName);
        verify(parkService).findParkById(parkId); // mockito verify that the findParkById method was called with the correct parkId
        verify(reservationService).getFutureReservations(parkId);
        verify(model).addAttribute("park", park);
        verify(model).addAttribute("reservations", reservations);
    }
}