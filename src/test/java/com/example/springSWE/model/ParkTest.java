package com.example.springSWE.model;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;

import java.time.LocalTime;

public class ParkTest {

	@Test
    public void testParkObjectCreation() {
        String name = "Central Park";
        String address = "123 Park Avenue";
        LocalTime openingTime = LocalTime.of(8, 0);
        LocalTime closingTime = LocalTime.of(20, 0);

        Park park = new Park();
        park.setName(name);
        park.setAddress(address);
        park.setOpeningTime(openingTime);
        park.setClosingTime(closingTime);

        assertEquals(name, park.getName());
        assertEquals(address, park.getAddress());
        assertEquals(openingTime, park.getOpeningTime());
        assertEquals(closingTime, park.getClosingTime());
    }

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
