package com.example.javaapp.controllers.ui;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/statistics")
public class StatisticsUiController {
    @GetMapping("/")
    public String statistics() {
        return "statistics.html";
    }

}
