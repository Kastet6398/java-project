package com.example.javaapp.controllers.ui;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class HomeUiController {
    @GetMapping("/")
    public String index() {
        return "index.html";
    }

}
