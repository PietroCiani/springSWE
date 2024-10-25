package com.example.springSWE.controller;

import com.example.springSWE.service.UserService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.ui.Model;

@Controller
@RequestMapping("/parks")
public class ParkController {
	
	@Autowired
    private UserService userService;

	@GetMapping
	public String showParksPage() {
		return "parks";
	}

	//TODO: @PostMapping

}
