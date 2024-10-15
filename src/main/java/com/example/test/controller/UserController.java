package com.example.test.controller;


import com.example.test.entity.User;
import com.example.test.dao.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
public class UserController {

    @Autowired
    private UserRepository repo;


    @GetMapping("/")
    public String home() {
        return "home";
    }

    @GetMapping("/login")
    public String loginPage() {
        return "login";
    }

    @GetMapping("/signup")
    public String homePage(Model model){
        model.addAttribute("user", new User());
        return "signup";
    }

    @PostMapping("/save")
    public String saveUser(User user) {
        repo.save(user);
        //TODO: check if user have been saved correctly
        return "login";
    }

}
