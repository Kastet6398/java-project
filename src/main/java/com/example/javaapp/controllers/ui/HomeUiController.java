package com.example.javaapp.controllers.ui;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class HomeUiController {
    @GetMapping("/")
    public String index() {
        return "index.html";
    }
    @GetMapping("/addContact")
    public String addContact() {
        return "add-friends.html";
    }
    @GetMapping("/Statistics")
    public String Statistics() {
        return "add-friends.html";
    }
    @GetMapping("/Notes")
    public String Notes() {
        return "add-friends.html";
    }

}
