package com.example.javaapp.controllers.ui;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/contacts")
public class ContactsUiController {
    @GetMapping("/create")
    public String addContact() {
        return "add-friends.html";
    }

}
