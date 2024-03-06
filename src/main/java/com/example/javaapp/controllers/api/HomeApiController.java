package com.example.javaapp.controllers.api;

import com.example.javaapp.models.dto.MessageDto;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
public class HomeApiController {
    @GetMapping("/")
    public Object index() {
        return new MessageDto("Hello!", true);
    }
}
