package com.example.springSWE.controller;

import com.example.springSWE.model.User;
import com.example.springSWE.service.UserService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.ui.Model;

@Controller
@RequestMapping("/signup")
public class SignupController {

    @Autowired
    private UserService userService;

    @GetMapping
    public String showSignupPage(){
        return "signup";
    }

    @PostMapping
    public String registerUser(@ModelAttribute User user, Model model) {
        if (userService.checkUserExists(user.getUsername(), user.getEmail())) {
            model.addAttribute("errorMessage", "Username or Email already exists");
            return "signup"; // Return signup + error
        }
        userService.saveUser(user);
        return "redirect:/login";
    }
}
