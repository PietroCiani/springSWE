package com.example.springSWE.controller;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

import com.example.springSWE.model.Park;
import com.example.springSWE.model.Reservation;
import com.example.springSWE.model.User;
import com.example.springSWE.service.ParkService;
import com.example.springSWE.service.ReservationService;
import com.example.springSWE.service.UserService;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.ui.ExtendedModelMap;
import org.springframework.ui.Model;

import java.security.Principal;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Arrays;
import java.util.List;

class ReservationControllerTest {

    @InjectMocks
    private ReservationController reservationController;

    @Mock
    private ReservationService reservationService;

    @Mock
    private ParkService parkService;

    @Mock
    private UserService userService;

    @Mock
    private Principal principal;

    private Model model;

    private User testUser;
    private Park testPark;
    private Reservation testReservation;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        model = new ExtendedModelMap();

        // test data
        testUser = new User();
        testUser.setUsername("testUser");
        testUser.setEmail("testUser@example.com");
        testUser.setPassword("password");

        testPark = new Park();
        testPark.setName("Test Park");
        testPark.setAddress("123 Test St");

        testReservation = new Reservation(LocalDate.now().plusDays(1), LocalTime.of(12, 0), 20, testUser, testPark);
    }

    @Test
    void testShowReservations() {
        when(principal.getName()).thenReturn("testUser");
        when(userService.getUserByUsername("testUser")).thenReturn(testUser);

        List<Reservation> reservations = List.of(testReservation);
        List<Park> parks = List.of(testPark);

        when(reservationService.getFutureReservationsByUser(testUser)).thenReturn(reservations);
        when(parkService.findAllParks()).thenReturn(parks);

        String viewName = reservationController.showReservations(principal, model);

        verify(userService, times(1)).getUserByUsername("testUser");
        verify(reservationService, times(1)).getFutureReservationsByUser(testUser);
        verify(parkService, times(1)).findAllParks();

        assertEquals(reservations, model.getAttribute("reservations"));
        assertEquals(parks, model.getAttribute("parks"));

        assertEquals("reservations", viewName);
    }
}
