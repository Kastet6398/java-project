package com.example.javaapp.controllers.api;

import com.example.javaapp.exceptions.AccessDeniedException;
import com.example.javaapp.exceptions.NotFoundException;
import com.example.javaapp.models.dto.ApiErrorResponse;
import com.example.javaapp.models.dto.LoginResponse;
import com.example.javaapp.models.dto.TaskRequest;
import com.example.javaapp.models.dto.TaskResponse;
import com.example.javaapp.models.entities.Task;
import com.example.javaapp.models.entities.User;
import com.example.javaapp.models.services.TaskService;
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
@RequestMapping(path = "/api/tasks", produces = MediaType.APPLICATION_JSON_VALUE)
public class TaskController {
    private final TaskService taskService;
    private final UserService userService;

    public TaskController(TaskService taskService, UserService userService) {
        this.taskService = taskService;
        this.userService = userService;
    }

    @Operation(summary = "Create a task")
    @ApiResponse(responseCode = "201")
    @ApiResponse(responseCode = "404", content = @Content(schema = @Schema(implementation = ApiErrorResponse.class)))
    @ApiResponse(responseCode = "409", content = @Content(schema = @Schema(implementation = ApiErrorResponse.class)))
    @ApiResponse(responseCode = "500", content = @Content(schema = @Schema(implementation = ApiErrorResponse.class)))
    @PostMapping("/create")
    public ResponseEntity<TaskResponse> create(@CookieValue(value = "token", defaultValue = "", required = false) String token, @Valid @RequestBody TaskRequest requestDto, HttpServletResponse response) {
        if (token.isBlank()) {
            throw new AccessDeniedException("You must be logged in to create a task.");
        }
        Optional<User> userOptional = userService.findByEncryptedEmail(token);
        if (userOptional.isPresent()) {
            User user = userOptional.get();
            Optional<Task> taskOptional = taskService.create(requestDto, user.id());
            if (taskOptional.isPresent()) {
                Task task = taskOptional.get();
                return ResponseEntity.ok(new TaskResponse(task.title(), task.description(), task.author(), task.deadline(), task.courseId(), task.id()));
            } else {
                return ResponseEntity.status(422).build();
            }
        } else {
            throw new AccessDeniedException("You must be logged in to create a task.");
        }
    }

    @Operation(summary = "Delete a task")
    @ApiResponse(responseCode = "200", content = @Content(schema = @Schema(implementation = LoginResponse.class)))
    @ApiResponse(responseCode = "401", content = @Content(schema = @Schema(implementation = ApiErrorResponse.class)))
    @ApiResponse(responseCode = "404", content = @Content(schema = @Schema(implementation = ApiErrorResponse.class)))
    @ApiResponse(responseCode = "500", content = @Content(schema = @Schema(implementation = ApiErrorResponse.class)))
    @GetMapping(value = "/delete")
    public ResponseEntity<Object> delete(@CookieValue(value = "token", defaultValue = "", required = false) String token, @RequestParam long id, HttpServletResponse response) {
        if (token.isBlank()) {
            throw new AccessDeniedException("You must be logged in to delete your task.");
        }
        Optional<User> userOptional = userService.findByEncryptedEmail(token);
        if (userOptional.isPresent()) {
            User user = userOptional.get();
            Optional<Task> taskOptional = taskService.findById(id);
            if (taskOptional.isPresent()) {
                if (taskOptional.get().author() == user.id()) {
                    if (taskService.delete(id)) {
                        return ResponseEntity.ok().build();
                    } else {
                        throw new NotFoundException("Task not found.");
                    }
                } else {
                    throw new AccessDeniedException("You cannot delete a task that does not belong to you.");
                }
            } else {
                throw new NotFoundException("Task not found.");
            }
        } else {
            throw new AccessDeniedException("You must be logged in to delete your task.");
        }
    }

    @Operation(summary = "List tasks")
    @ApiResponse(responseCode = "200", content = @Content(schema = @Schema(implementation = LoginResponse.class)))
    @ApiResponse(responseCode = "404", content = @Content(schema = @Schema(implementation = ApiErrorResponse.class)))
    @ApiResponse(responseCode = "500", content = @Content(schema = @Schema(implementation = ApiErrorResponse.class)))
    @GetMapping(value = "/list")
    public ResponseEntity<Object> list(@CookieValue(value = "token", defaultValue = "", required = false) String token, HttpServletResponse response) {
        if (token.isBlank()) {
            throw new AccessDeniedException("You must be logged in to view your tasks.");
        }
        Optional<User> userOptional = userService.findByEncryptedEmail(token);
        if (userOptional.isPresent()) {
            User user = userOptional.get();
            return ResponseEntity.ok(taskService.listTasksForUser(user));
        } else {
            throw new AccessDeniedException("You must be logged in to view your tasks.");
        }
    }
}
