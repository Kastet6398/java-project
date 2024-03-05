package com.example.javaapp.controllers;

import com.example.javaapp.models.BaseModel;
import com.example.javaapp.models.MessageModel;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/")
public class ApiController {

    @GetMapping("/")
    public BaseModel index() {
        return new MessageModel("Hi!", true);
    }
}
