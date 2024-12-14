package com.example.springSWE.model;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class UserTest {

    @Test
    public void testUserObjectCreation() {
        User user = new User();

        user.setUsername("testUser");
        user.setEmail("test@example.com");
        user.setPassword("password123");

        assertEquals("testUser", user.getUsername());
        assertEquals("test@example.com", user.getEmail());
        assertEquals("password123", user.getPassword());
    }
}
