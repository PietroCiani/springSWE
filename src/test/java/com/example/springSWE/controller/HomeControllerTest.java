package com.example.springSWE.controller;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertEquals;

class HomeControllerTest {

    private HomeController homeController = new HomeController();

    @Test
    void testHomePageMapping() {
        String viewName = homeController.home();
        assertEquals("home", viewName);
    }

    @Test
    void testLoginPageMapping() {
        String viewName = homeController.loginPage();
        assertEquals("login", viewName);
    }
}