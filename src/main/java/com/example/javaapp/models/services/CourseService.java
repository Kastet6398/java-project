package com.example.javaapp.models.services;

import com.example.javaapp.exceptions.DuplicateException;
import com.example.javaapp.exceptions.NotFoundException;
import com.example.javaapp.models.dto.CourseRequest;
import com.example.javaapp.models.entities.Course;
import com.example.javaapp.models.entities.User;
import com.example.javaapp.models.repositories.CourseRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@Transactional(readOnly = true)
public class CourseService {
    private final CourseRepository repository;
    private final UserService userRepository;

    public CourseService(CourseRepository repository, UserService userRepository) {
        this.repository = repository;
        this.userRepository = userRepository;
    }

    @Transactional
    public Optional<Course> create(CourseRequest request, long userId) {
        String title = request.title();
        Optional<Course> existingCourse = repository.findCourse(title);
        if (existingCourse.isPresent()) {
            throw new DuplicateException(STR."Course with the title '\{title}' already exists.");
        }

        List<User> invitedUsers = new ArrayList<>();

        Optional<User> currentUserOptional = userRepository.findById(userId);
        if (currentUserOptional.isPresent()) {
            invitedUsers.add(currentUserOptional.get());
        } else {
            throw new NotFoundException("Current user not found.");
        }
        for (long invitedUserId : request.invitedUsers()) {
            Optional<User> userOptional = userRepository.findById(invitedUserId);
            if (userOptional.isPresent()) {
                invitedUsers.add(userOptional.get());
            } else {
                throw new NotFoundException(STR."Cannot fetch the user with ID \{invitedUserId}");
            }
        }

        Course task = new Course(request.title(), request.description(), userId, invitedUsers, -1);
        return repository.createCourse(task);
    }

    public boolean delete(long id) {
        return repository.deleteTask(id);
    }

    public Optional<Course> findById(long id) {
        return repository.findCourseById(id);
    }

    @Transactional
    public List<Course> listCoursesForUser(User user) {
        return repository.listCourses(user.id());
    }

}
