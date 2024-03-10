package com.example.javaapp.controllers.api;

import com.example.javaapp.exceptions.AccessDeniedException;
import com.example.javaapp.exceptions.InternalServerException;
import com.example.javaapp.exceptions.NotFoundException;
import com.example.javaapp.models.dto.ApiErrorResponse;
import com.example.javaapp.models.dto.CourseRequest;
import com.example.javaapp.models.dto.CourseResponse;
import com.example.javaapp.models.dto.LoginResponse;
import com.example.javaapp.models.entities.Course;
import com.example.javaapp.models.entities.User;
import com.example.javaapp.models.services.CourseService;
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

import java.util.Optional;

@RestController
@RequestMapping(path = "/api/course", produces = MediaType.APPLICATION_JSON_VALUE)
public class CourseController {
    private final CourseService courseService;
    private final UserService userService;

    public CourseController(CourseService courseService, UserService userService) {
        this.courseService = courseService;
        this.userService = userService;
    }

    @Operation(summary = "Create a course")
    @ApiResponse(responseCode = "201")
    @ApiResponse(responseCode = "404", content = @Content(schema = @Schema(implementation = ApiErrorResponse.class)))
    @ApiResponse(responseCode = "409", content = @Content(schema = @Schema(implementation = ApiErrorResponse.class)))
    @ApiResponse(responseCode = "500", content = @Content(schema = @Schema(implementation = ApiErrorResponse.class)))
    @PostMapping("/create")
    public Object create(@CookieValue(value = "token", defaultValue = "", required = false) String token, @Valid @RequestBody CourseRequest requestDto, HttpServletResponse response) {
        if (token.isBlank()) {
            throw new AccessDeniedException("You must be logged in to create courses.");
        }
        Optional<User> userOptional = userService.findByEncryptedEmail(token);
        if (userOptional.isPresent()) {
            User user = userOptional.get();

            Optional<Course> courseOptional = courseService.create(requestDto, user.id());
            if (courseOptional.isPresent()) {
                Course course = courseOptional.get();
                return ResponseEntity.ok(new CourseResponse(course.title(), course.description(), course.author(), course.id()));
            } else {
                throw new InternalServerException("Course creation failed.");
            }
        } else {
            throw new AccessDeniedException("You must be logged in to create a course.");
        }
    }

    @Operation(summary = "Delete a course")
    @ApiResponse(responseCode = "200", content = @Content(schema = @Schema(implementation = LoginResponse.class)))
    @ApiResponse(responseCode = "401", content = @Content(schema = @Schema(implementation = ApiErrorResponse.class)))
    @ApiResponse(responseCode = "404", content = @Content(schema = @Schema(implementation = ApiErrorResponse.class)))
    @ApiResponse(responseCode = "500", content = @Content(schema = @Schema(implementation = ApiErrorResponse.class)))
    @GetMapping(value = "/delete")
    public Object delete(@CookieValue(value = "token", defaultValue = "", required = false) String token, @RequestParam long id, HttpServletResponse response) {
        Optional<User> userOptional = userService.findByEncryptedEmail(token);
        if (userOptional.isPresent()) {
            User user = userOptional.get();
            Optional<Course> courseOptional = courseService.findById(id);
            if (courseOptional.isPresent()) {
                if (courseOptional.get().author() == user.id()) {
                    if (courseService.delete(id)) {
                        return ResponseEntity.ok().build();
                    } else {
                        throw new InternalServerException("Something went wrong.");
                    }
                } else {
                    throw new AccessDeniedException("You cannot delete a course that does not belong to you.");
                }
            } else {
                throw new NotFoundException("Course not found");
            }
        } else {
            throw new AccessDeniedException("You must be logged in to delete your course.");
        }
    }

    @Operation(summary = "List courses")
    @ApiResponse(responseCode = "200", content = @Content(schema = @Schema(implementation = LoginResponse.class)))
    @ApiResponse(responseCode = "404", content = @Content(schema = @Schema(implementation = ApiErrorResponse.class)))
    @ApiResponse(responseCode = "500", content = @Content(schema = @Schema(implementation = ApiErrorResponse.class)))
    @GetMapping(value = "/list")
    public Object list(@CookieValue(value = "token", defaultValue = "", required = false) String token, HttpServletResponse response) {
        Optional<User> userOptional = userService.findByEncryptedEmail(token);
        if (userOptional.isPresent()) {
            User user = userOptional.get();
            return ResponseEntity.ok(courseService.listCoursesForUser(user));
        } else {
            throw new AccessDeniedException("You must be logged in to view your courses.");
        }
    }
}
