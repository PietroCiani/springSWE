package com.example.springSWE.controller;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.Optional;

import org.springframework.boot.test.context.SpringBootTest;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.ExtendedModelMap;
import org.springframework.ui.Model;

import com.example.springSWE.dao.UserRepository;
import com.example.springSWE.model.User;
import com.example.springSWE.service.UserService;

@SpringBootTest
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class SignupControllerTest {
 
	@Autowired
    private SignupController signupController;
	@Autowired
	private UserService userService;
	@Autowired
	private UserRepository userRepository;


	@Test
	void testRegisterUser() {
		User user = new User();
		user.setUsername("name");
		user.setEmail("email@mail.com");
		user.setPassword("password");
		Model model = new ExtendedModelMap();
		signupController.registerUser(user, model);

		User savedUser = userRepository.findByUsername("name").get();
		assertEquals(user.getUsername(), savedUser.getUsername(), "Usernames should match");
	}

	@Test
	void testShowSignupPage() {
		String page = signupController.showSignupPage();
		assertEquals("signup", page, "Page should be signup");
	}
}
