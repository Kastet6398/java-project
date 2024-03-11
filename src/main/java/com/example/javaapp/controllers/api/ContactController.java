package com.example.javaapp.controllers.api;

import com.example.javaapp.exceptions.AccessDeniedException;
import com.example.javaapp.exceptions.InternalServerException;
import com.example.javaapp.exceptions.NotFoundException;
import com.example.javaapp.models.dto.ApiErrorResponse;
import com.example.javaapp.models.dto.ContactRequest;
import com.example.javaapp.models.dto.ContactResponse;
import com.example.javaapp.models.dto.LoginResponse;
import com.example.javaapp.models.entities.Contact;
import com.example.javaapp.models.entities.User;
import com.example.javaapp.models.services.ContactService;
import com.example.javaapp.models.services.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping(path = "/api/contact", produces = MediaType.APPLICATION_JSON_VALUE)
public class ContactController {
    private final ContactService contactService;
    private final UserService userService;

    public ContactController(ContactService contactService, UserService userService) {
        this.contactService = contactService;
        this.userService = userService;
    }

    @Operation(summary = "Add a contact")
    @ApiResponse(responseCode = "201", content = @Content(schema = @Schema(implementation = ContactResponse.class)))
    @ApiResponse(responseCode = "401", content = @Content(schema = @Schema(implementation = ApiErrorResponse.class)))
    @ApiResponse(responseCode = "404", content = @Content(schema = @Schema(implementation = ApiErrorResponse.class)))
    @ApiResponse(responseCode = "409", content = @Content(schema = @Schema(implementation = ApiErrorResponse.class)))
    @ApiResponse(responseCode = "500", content = @Content(schema = @Schema(implementation = ApiErrorResponse.class)))
    @PostMapping("/add")
    public Object addContact(@CookieValue(value = "token", defaultValue = "", required = false) String token,
                             @Valid @RequestBody ContactRequest requestDto,
                             HttpServletResponse response) {
        if (token.isBlank()) {
            throw new AccessDeniedException("You must be logged in to add contacts.");
        }

        Optional<User> userOptional = userService.findByEncryptedEmail(token);
        if (userOptional.isPresent()) {
            User owner = userOptional.get();
            Optional<User> userOptional2 = userService.findById(requestDto.id());
            if (userOptional2.isPresent()) {
                Optional<Long> contactId = contactService.addContact(owner, requestDto.name(), userOptional2.get());
                if (contactId.isPresent()) {
                    return ResponseEntity.status(201).body(new ContactResponse(requestDto.id(), contactId.get()));
                } else {
                    throw new InternalServerException("Contact creation failed.");
                }
            }
        } else {
            throw new AccessDeniedException("You must be logged in to add a contact.");
        }
        return null;
    }

    @Operation(summary = "Delete a contact")
    @ApiResponse(responseCode = "200")
    @ApiResponse(responseCode = "401", content = @Content(schema = @Schema(implementation = ApiErrorResponse.class)))
    @ApiResponse(responseCode = "404", content = @Content(schema = @Schema(implementation = ApiErrorResponse.class)))
    @ApiResponse(responseCode = "500", content = @Content(schema = @Schema(implementation = ApiErrorResponse.class)))
    @GetMapping(value = "/delete")
    public Object deleteContact(@CookieValue(value = "token", defaultValue = "", required = false) String token,
                                @RequestParam long id,
                                HttpServletResponse response) {
        Optional<User> userOptional = userService.findByEncryptedEmail(token);
        if (userOptional.isPresent()) {
            User owner = userOptional.get();
            if (contactService.deleteContact(id)) {
                return ResponseEntity.ok().build();
            } else {
                throw new InternalServerException("Something went wrong.");
            }
        } else {
            throw new AccessDeniedException("You must be logged in to delete a contact.");
        }
    }

    @Operation(summary = "List contacts")
    @ApiResponse(responseCode = "200", content = @Content(schema = @Schema(implementation = LoginResponse.class)))
    @ApiResponse(responseCode = "401", content = @Content(schema = @Schema(implementation = ApiErrorResponse.class)))
    @ApiResponse(responseCode = "404", content = @Content(schema = @Schema(implementation = ApiErrorResponse.class)))
    @ApiResponse(responseCode = "500", content = @Content(schema = @Schema(implementation = ApiErrorResponse.class)))
    @GetMapping(value = "/list")
    public Object listContacts(@CookieValue(value = "token", defaultValue = "", required = false) String token,
                               HttpServletResponse response) {
        Optional<User> userOptional = userService.findByEncryptedEmail(token);
        if (userOptional.isPresent()) {
            User owner = userOptional.get();
            List<Map<String, Object>> contacts = contactService.findContactsForUser(owner);
            return ResponseEntity.ok(contacts);
        } else {
            throw new AccessDeniedException("You must be logged in to view your contacts.");
        }
    }
}
