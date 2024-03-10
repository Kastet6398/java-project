package com.example.javaapp.models.services;

import com.example.javaapp.exceptions.DuplicateException;
import com.example.javaapp.models.dto.CourseRequest;
import com.example.javaapp.models.entities.Course;
import com.example.javaapp.models.entities.User;
import com.example.javaapp.models.repositories.CourseRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@Transactional(readOnly = true)
public class CourseService {
    private final CourseRepository repository;

    public CourseService(CourseRepository repository) {
        this.repository = repository;
    }

    @Transactional
    public Optional<Course> create(CourseRequest request, long userId) {
        String title = request.title();
        Optional<Course> existingCourse = repository.findCourse(title);
        if (existingCourse.isPresent()) {
            throw new DuplicateException(STR."Course with the title '\{title}' already exists.");
        }

        Course task = new Course(request.title(), request.description(), userId, -1);
        return repository.createCourse(task);
    }

    public boolean delete(long id) {
        return repository.deleteTask(id);
    }

    public Optional<Course> findById(long id) {
        return repository.findTaskById(id);
    }

    @Transactional
    public List<Course> listCoursesForUser(User user) {
        return repository.listCourses(user.id());
    }

}
