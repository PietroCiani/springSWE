package com.example.springSWE.model;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.assertFalse;

import java.time.LocalTime;

public class ParkTest {
	@Test
	void testIsOpen() {
		Park park = new Park();
		park.setOpeningTime(LocalTime.of(8, 0));
		park.setClosingTime(LocalTime.of(20, 0));
		assertTrue(park.isOpen(LocalTime.of(10, 0)));
		assertTrue(park.isOpen(LocalTime.of(8, 0)));
		assertTrue(park.isOpen(LocalTime.of(20, 0)));
		assertFalse(park.isOpen(LocalTime.of(7, 59)));
		assertFalse(park.isOpen(LocalTime.of(20, 1)));
	}
}
