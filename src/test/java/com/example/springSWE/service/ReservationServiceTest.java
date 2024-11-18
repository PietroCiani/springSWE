package com.example.springSWE.service;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import com.example.springSWE.dao.UserRepository;
import com.example.springSWE.dao.ParkRepository;
import com.example.springSWE.dao.ReservationRepository;
import com.example.springSWE.model.User;
import com.example.springSWE.model.Park;
import com.example.springSWE.model.Reservation;

@SpringBootTest
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class ReservationServiceTest {

	@Autowired
	private UserRepository userRepository;

    @Autowired
    private ParkRepository parkRepository;

    @Autowired
    private ReservationRepository reservationRepository;

	@Autowired
    private UserService userService;


    private User testUser;
	private User testUser2;
    private Park testPark;
	private Park testPark2;
    private Reservation testReservation;
	private Reservation testReservation2;
	private Reservation concurrentReservation;
	private Reservation pastReservation;


    @BeforeEach
    public void setUp() {
        reservationRepository.deleteAll();
        userRepository.deleteAll();
        parkRepository.deleteAll();

        testUser = new User();
        testUser.setUsername("testuser");
		testUser.setEmail("unclepear@pear.com");
        testUser.setPassword("password123");
		userService.saveUser(testUser);

		testUser2 = new User();
		testUser2.setUsername("testuser2");
		testUser2.setEmail("ziopera@pera.com");
		testUser2.setPassword("password123");
		userService.saveUser(testUser2);

        testPark = new Park();
        testPark.setName("Test Park");
        testPark.setAddress("123 Test St");
        testPark = parkRepository.save(testPark);


        testReservation = new Reservation(LocalDate.now().plusDays(1), LocalTime.of(19, 0), 20, testUser, testPark);
		testReservation2 = new Reservation(LocalDate.now().plusDays(1), LocalTime.of(18, 35), 15, testUser, testPark);
		concurrentReservation = new Reservation(LocalDate.now().plusDays(1), LocalTime.of(19, 5), 25, testUser, testPark2);
		pastReservation = new Reservation(LocalDate.now().minusDays(1), LocalTime.of(1, 20), 10, testUser2, testPark);

		testReservation = reservationRepository.save(testReservation);
		testReservation2 = reservationRepository.save(testReservation2);
		concurrentReservation = reservationRepository.save(concurrentReservation);
		pastReservation = reservationRepository.save(pastReservation);
		
    }

	@Test
	void testDeleteReservation() {
		reservationRepository.deleteById(testReservation.getId());
		assertEquals(
			Optional.empty(),
			reservationRepository.findById(testReservation.getId())
		);
	}

	@Test
	void testGetAllReservationsByPark() {
		assertEquals(
			3,
			reservationRepository.findByParkId(testPark.getId()).size()
		);
	}

	@Test
	void testGetFutureReservations() {
		assertEquals(
			3,
			reservationRepository.findByParkId(testPark.getId()).size()
		);
	}

	@Test
	void testGetFutureReservationsByUser() {
		assertEquals(
			3,
			reservationRepository.findByUserId(testUser.getId()).size()
		);
	}

	@Test
	void testGetOngoingReservation() {
		assertEquals(
			testReservation.getId(),
			reservationRepository.findOngoingReservationByParkDateTime(testPark.getId(), LocalDate.now().plusDays(1), LocalTime.of(19, 10)).get(0).getId()
		);
	}

	@Test
	void testGetReservationsByParkAndDate() {
		assertEquals(
			2,
			reservationRepository.findByParkIdAndDate(testPark.getId(), LocalDate.now().plusDays(1)).size()
		);
	}




	@Test
	void testHasConcurrentReservation() {
		boolean hasOverlap = hasOverlappingReservation(concurrentReservation);
		assertTrue(hasOverlap, "Expected to find an overlapping reservation.");
	}

	public boolean hasOverlappingReservation(Reservation newReservation) {
		List<Reservation> existingReservations = reservationRepository.findByUserAndDate(newReservation.getUser(), newReservation.getDate());
	
		for (Reservation reservation : existingReservations) {
			if (reservationsOverlap(newReservation, reservation)) {
				return true;
			}
		}
		return false;
	}
	
	private boolean reservationsOverlap(Reservation r1, Reservation r2) {
		return !r1.getEndTime().isBefore(r2.getStartTime()) && !r1.getStartTime().isAfter(r2.getEndTime());
	}

	@AfterAll
	public void tearDown() {
		reservationRepository.deleteAll();
		userRepository.deleteAll();
		parkRepository.deleteAll();
	}
}
