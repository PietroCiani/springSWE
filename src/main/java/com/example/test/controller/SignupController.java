package com.example.test.controller;

import com.example.test.entity.User;
import com.example.test.entity.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.ui.Model;

@RestController
@RequestMapping("/signup")
public class SignupController {

    @Autowired
    private UserService userService;

    @PostMapping
    public String registerUser(@ModelAttribute User user, Model model) {
        if (userService.checkUserExists(user.getUsername(), user.getEmail())) {
            model.addAttribute("errorMessage", "Username or Email already exists");
            return "signup"; // Return signup + error
        }
        userService.saveUser(user);
        return "redirect:/login";
    }

    @GetMapping("/signup")
    public String homePage(Model model){
        model.addAttribute("user", new User());
        return "signup";
    }

    @PostMapping("/save")
    public String saveUser(User user) {
        userService.saveUser(user);
        return "login";
    }
}

