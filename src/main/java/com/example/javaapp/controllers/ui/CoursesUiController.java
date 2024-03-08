package com.example.javaapp.controllers.ui;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/courses")
public class CoursesUiController {
    @GetMapping("/")
    public String courses() {
        return "courses.html";
    }

}
