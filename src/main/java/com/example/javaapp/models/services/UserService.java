package com.example.javaapp.models.services;
import java.util.Optional;

import com.example.javaapp.exceptions.DuplicateException;
import com.example.javaapp.models.dto.SignupRequest;
import com.example.javaapp.models.entities.User;
import com.example.javaapp.models.repositories.UserRepository;
import com.example.javaapp.utils.JwtHelper;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
public class UserService {
    private final UserService userService;

    private final UserRepository repository;
    private final PasswordEncoder passwordEncoder;

    public UserService(UserService userService, UserRepository repository, PasswordEncoder passwordEncoder) {
        this.userService = userService;
        this.repository = repository;
        this.passwordEncoder = passwordEncoder;
    }

    @Transactional
    public Optional<User> signup(SignupRequest request) {
        String email = request.email();
        Optional<User> existingUser = repository.findByEmail(email);
        if (existingUser.isPresent()) {
            throw new DuplicateException(STR."User with the email address '\{email}' already exists.");
        }

        String hashedPassword = passwordEncoder.encode(request.password());
        User user = new User(request.name(), email, hashedPassword, request.phone(), -1);
        return repository.add(user);
    }

    public Optional<User> findByEncryptedEmail(String token) {
        return repository.findByEmail(JwtHelper.extractUsername(token));
    }

    public Optional<User> findById(long invitedUserId) {
        return repository.findById(invitedUserId);
    }
}
