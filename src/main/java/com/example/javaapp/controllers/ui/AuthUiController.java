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
    private final AuthenticationManager authenticationManager;

    private final UserService userService;
    private final UserRepository userRepository;

    @Autowired
    public AuthUiController(AuthenticationManager authenticationManager, UserService userService, UserRepository userRepository) {
        this.authenticationManager = authenticationManager;
        this.userService = userService;
        this.userRepository = userRepository;
    }

    @GetMapping("/signup")
    public String showSignupForm(Model model) {
        model.addAttribute("signupRequest", new SignupRequest());
        return "signup";
    }

    @GetMapping("/login")
    public String showLoginForm(Model model) {
        model.addAttribute("loginRequest", new LoginRequest());
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