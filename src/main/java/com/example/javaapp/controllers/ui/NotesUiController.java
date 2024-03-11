package com.example.javaapp.controllers.ui;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/notes")
public class NotesUiController {
    @GetMapping("/")
    public String notes() {
        return "add-friends.html";
    }

}
