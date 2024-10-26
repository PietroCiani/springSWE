package com.example.springSWE.controller;

import com.example.springSWE.model.Park;
import com.example.springSWE.service.ParkService;

import java.util.List;
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
	public String showParks(Model model) {
		List<Park> parks = parkService.findAllParks();
        model.addAttribute("parks", parks);
        return "parks";
	}

	//TODO: @PostMapping

}
