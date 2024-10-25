package com.example.springSWE.controller;

import com.example.springSWE.service.ParkService;
import com.example.springSWE.service.ParkService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.ui.Model;

@Controller
@RequestMapping("/parks")
public class ParkController {
	
	@Autowired
    private ParkService parkService;

	@GetMapping
	public String showParksPage() {
		return "parks";
	}

	//TODO: @PostMapping

}
