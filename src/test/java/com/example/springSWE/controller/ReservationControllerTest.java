package com.example.springSWE.controller;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

import com.example.springSWE.model.Park;
import com.example.springSWE.model.Reservation;
import com.example.springSWE.model.User;
import com.example.springSWE.service.ParkService;
import com.example.springSWE.service.ReservationService;
import com.example.springSWE.service.UserService;

import jakarta.servlet.http.HttpServletRequest;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.ui.ExtendedModelMap;
import org.springframework.ui.Model;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.web.servlet.mvc.support.RedirectAttributesModelMap;

import java.security.Principal;
import java.time.LocalDate;
import java.time.LocalTime;
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

    @Mock
    private HttpServletRequest request;


    private RedirectAttributes redirectAttributes;

    private Model model;

    private User testUser;
    private Park testPark;
    private Reservation testReservation;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        redirectAttributes = new RedirectAttributesModelMap();
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

    @Test
    void testCreateReservationSuccess() {
        Long parkId = 1L;
        LocalDate date = LocalDate.now().plusDays(1);
        LocalTime startTime = LocalTime.of(10, 0);
        int duration = 15;
        User user = new User();
        user.setUsername("testUser");
        Park park = mock(Park.class);
    
        //mock behaviors
        when(principal.getName()).thenReturn("testUser");
        when(userService.getUserByUsername("testUser")).thenReturn(user);
        when(parkService.findParkById(parkId)).thenReturn(park);
        when(park.isOpen(startTime)).thenReturn(true);
        when(park.isOpen(startTime.plusMinutes(duration))).thenReturn(true);
        when(reservationService.findReservationsForParkAndDateWithinTimeRange(parkId, date, startTime, startTime.plusMinutes(duration)))
                .thenReturn(List.of());
        when(reservationService.hasConcurrentReservation(user, date, startTime, duration)).thenReturn(false);
        when(request.getHeader("Referer")).thenReturn("/previous");
    
        String viewName = reservationController.createReservation(parkId, date, startTime, duration, principal, redirectAttributes, request);
    
        verify(reservationService).saveReservation(any(Reservation.class)); //ensure saveReservation is called
        assertEquals("redirect:/reservations", viewName);
        assertEquals("Reservation successfully created!", redirectAttributes.getFlashAttributes().get("success"));
    }
    
    @Test
    void testCreateReservationInPast() {
        Long parkId = 1L;
        LocalDate date = LocalDate.now().minusDays(1); //date in the past
        LocalTime startTime = LocalTime.of(10, 0);
        when(request.getHeader("Referer")).thenReturn("/previous");

        String viewName = reservationController.createReservation(parkId, date, startTime, 10, principal, redirectAttributes, request);

        //expect failing
        assertEquals("redirect:/previous", viewName);
        assertEquals("Cannot book a reservation in the past.", redirectAttributes.getFlashAttributes().get("error"));
    }

    @Test
    void testCreateReservationOverlappingParkReservation() {
        Long parkId = 1L;
        LocalDate date = LocalDate.now().plusDays(1);
        LocalTime startTime = LocalTime.of(10, 0);
        int duration = 20;
        when(parkService.findParkById(parkId)).thenReturn(new Park());
        when(reservationService.findReservationsForParkAndDateWithinTimeRange(parkId, date, startTime, startTime.plusMinutes(duration)))
                .thenReturn(List.of(new Reservation()));
        when(request.getHeader("Referer")).thenReturn(null);

        String viewName = reservationController.createReservation(parkId, date, startTime, duration, principal, redirectAttributes, request);

        //expect failing
        assertEquals("redirect:/", viewName);
        assertEquals("Selected time is already booked.", redirectAttributes.getFlashAttributes().get("error"));
    }

    @Test
    void testCreateReservationOverlappingUserReservation() {
        Long parkId = 1L;
        LocalDate date = LocalDate.now().plusDays(1);
        LocalTime startTime = LocalTime.of(10, 0);
        int duration = 10;
        User user = new User();
        when(principal.getName()).thenReturn("testUser");
        when(userService.getUserByUsername("testUser")).thenReturn(user);
        when(reservationService.hasConcurrentReservation(user, date, startTime, duration)).thenReturn(true);

        String viewName = reservationController.createReservation(parkId, date, startTime, duration, principal, redirectAttributes, request);

        assertEquals("redirect:/", viewName);
        assertEquals("You already have a reservation at this time.", redirectAttributes.getFlashAttributes().get("error"));
    }

    @Test
    void testCreateReservationParkClosed() {
        Long parkId = 1L;
        LocalDate date = LocalDate.now().plusDays(1);
        LocalTime startTime = LocalTime.of(23, 30);
        int duration = 30;
        Park park = mock(Park.class);
        when(parkService.findParkById(parkId)).thenReturn(park);
        when(park.isOpen(startTime)).thenReturn(false);

        String viewName = reservationController.createReservation(parkId, date, startTime, duration, principal, redirectAttributes, request);

        assertEquals("redirect:/", viewName);
        assertEquals("Park is closed at selected time.", redirectAttributes.getFlashAttributes().get("error"));
    }

    @Test
    void testCreateReservationException() {
        Long parkId = 1L;
        LocalDate date = LocalDate.now().plusDays(1);
        LocalTime startTime = LocalTime.of(10, 0);
        int duration = 30;
        when(parkService.findParkById(parkId)).thenThrow(new RuntimeException("Park not found"));

        String viewName = reservationController.createReservation(parkId, date, startTime, duration, principal, redirectAttributes, request);

        assertEquals("redirect:/", viewName);
        assertTrue(redirectAttributes.getFlashAttributes().get("error").toString().contains("Could not create reservation: Park not found"));
    }

    @Test
    void testDeleteReservationSuccess() {
        Long reservationId = 1L;
        doNothing().when(reservationService).deleteReservation(reservationId);

        String viewName = reservationController.deleteReservation(reservationId, redirectAttributes);

        verify(reservationService, times(1)).deleteReservation(reservationId);
        assertEquals("Reservation successfully deleted!", redirectAttributes.getFlashAttributes().get("success"));
        assertEquals("redirect:/reservations", viewName);
    }

    @Test
    void testDeleteReservationFailure() {
        Long reservationId = 1L;
        doThrow(new RuntimeException("Reservation not found")).when(reservationService).deleteReservation(reservationId);

        String viewName = reservationController.deleteReservation(reservationId, redirectAttributes);

        verify(reservationService, times(1)).deleteReservation(reservationId);
        String errorMessage = (String) redirectAttributes.getFlashAttributes().get("error");
        assertNotNull(errorMessage);
        assertTrue(errorMessage.contains("Could not delete reservation: Reservation not found"));
        assertEquals("redirect:/reservations", viewName);
    }

}
