package com.example.javaapp.controllers.ui;


import com.example.javaapp.models.dto.LoginRequest;
import com.example.javaapp.models.dto.SignupRequest;
import com.example.javaapp.models.dto.LoginResponse;
import com.example.javaapp.models.repositories.UserRepository;
import com.example.javaapp.models.services.UserService;
import com.example.javaapp.utils.JwtHelper;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/auth")
public class AuthUiController {
    @GetMapping("/signup")
    public String showSignupForm() {
        return "signup";
    }

    @GetMapping("/login")
    public String showLoginForm() {
        return "login";
    }

    @GetMapping("/logout")
    public String logout(HttpServletResponse response) {
        Cookie cookie = new Cookie("token", "");
        cookie.setMaxAge(-1);
        response.addCookie(cookie);
        return "redirect:/auth/login";
    }
}