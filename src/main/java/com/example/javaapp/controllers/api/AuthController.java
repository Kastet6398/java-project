package com.example.javaapp.controllers.api;

import com.example.javaapp.models.dto.*;
import com.example.javaapp.models.entities.User;
import com.example.javaapp.models.repositories.UserRepository;
import com.example.javaapp.models.services.UserService;
import com.example.javaapp.utils.JwtHelper;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(path = "/api/auth", produces = MediaType.APPLICATION_JSON_VALUE)
public class AuthController {

    private final AuthenticationManager authenticationManager;
    private final UserRepository userRepository;
    private final UserService userService;

    public AuthController(AuthenticationManager authenticationManager, UserService userService,
                          UserRepository userRepository) {
        this.authenticationManager = authenticationManager;
        this.userService = userService;
        this.userRepository = userRepository;
    }

    @Operation(summary = "Signup user")
    @ApiResponse(responseCode = "201")
    @ApiResponse(responseCode = "404", content = @Content(schema = @Schema(implementation = ApiErrorResponse.class)))
    @ApiResponse(responseCode = "409", content = @Content(schema = @Schema(implementation = ApiErrorResponse.class)))
    @ApiResponse(responseCode = "500", content = @Content(schema = @Schema(implementation = ApiErrorResponse.class)))
    @PostMapping("/signup")
    public Object signup(@Valid @RequestBody SignupRequest requestDto, HttpServletResponse response) {
        User user = userService.signup(requestDto);
        String token = JwtHelper.generateToken(user.email());
        response.addCookie(new Cookie("token", token));
        return ResponseEntity.ok(new LoginResponse(user.email(), token));
    }

    @Operation(summary = "Authenticate user and return token")
    @ApiResponse(responseCode = "200", content = @Content(schema = @Schema(implementation = LoginResponse.class)))
    @ApiResponse(responseCode = "401", content = @Content(schema = @Schema(implementation = ApiErrorResponse.class)))
    @ApiResponse(responseCode = "404", content = @Content(schema = @Schema(implementation = ApiErrorResponse.class)))
    @ApiResponse(responseCode = "500", content = @Content(schema = @Schema(implementation = ApiErrorResponse.class)))
    @PostMapping(value = "/login")
    public Object login(@Valid @RequestBody LoginRequest request, HttpServletResponse response) {
        authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(request.email(), request.password()));

        String token = JwtHelper.generateToken(request.email());

        response.addCookie(new Cookie("token", token));
        return ResponseEntity.ok(new LoginResponse(request.email(), token));
    }

    @Operation(summary = "Returns the user by token cookie")
    @ApiResponse(responseCode = "200", content = @Content(schema = @Schema(implementation = LoginResponse.class)))
    @ApiResponse(responseCode = "401", content = @Content(schema = @Schema(implementation = ApiErrorResponse.class)))
    @ApiResponse(responseCode = "404", content = @Content(schema = @Schema(implementation = ApiErrorResponse.class)))
    @ApiResponse(responseCode = "500", content = @Content(schema = @Schema(implementation = ApiErrorResponse.class)))
    @GetMapping(value = "/user")
    public Object getUser(@CookieValue(value = "token", defaultValue = "", required = false) String token) {
        if (token.isBlank()) {
            return ResponseEntity.ok().build();
        }
        User user = userRepository.findByEmail(JwtHelper.extractUsername(token)).orElse(null);
        return user != null ? ResponseEntity.ok(new UserResponse(user.name(), user.email(), user.phone())) : ResponseEntity.ok().build();
    }

    @Operation(summary = "Logout user")
    @ApiResponse(responseCode = "200", content = @Content(schema = @Schema(implementation = LoginResponse.class)))
    @ApiResponse(responseCode = "404", content = @Content(schema = @Schema(implementation = ApiErrorResponse.class)))
    @ApiResponse(responseCode = "500", content = @Content(schema = @Schema(implementation = ApiErrorResponse.class)))
    @GetMapping(value = "/logout")
    public Object logout(HttpServletResponse response) {
        Cookie cookie = new Cookie("token", "");
        cookie.setMaxAge(-1);
        response.addCookie(cookie);
        return ResponseEntity.status(HttpStatus.OK).build();
    }
}
