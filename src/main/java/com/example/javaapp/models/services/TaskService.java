package com.example.javaapp.models.services;

import com.example.javaapp.exceptions.DuplicateException;
import com.example.javaapp.models.dto.TaskRequest;
import com.example.javaapp.models.entities.Task;
import com.example.javaapp.models.entities.User;
import com.example.javaapp.models.repositories.TaskRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@Transactional(readOnly = true)
public class TaskService {
    private final TaskRepository repository;

    public TaskService(TaskRepository repository) {
        this.repository = repository;
    }

    @Transactional
    public Optional<Task> create(TaskRequest request, long userId) {
        String title = request.title();
        Optional<Task> existingTask = repository.findTask(title);
        if (existingTask.isPresent()) {
            throw new DuplicateException(STR."Task with the title '\{title}' already exists.");
        }

        Task task = new Task(request.title(), request.description(), userId, request.deadline(), request.courseId(), -1);
        return repository.createTask(task);
    }

    public boolean delete(long id) {
        return repository.deleteTask(id);
    }

    public Optional<Task> findById(long id) {
        return repository.findTaskById(id);
    }

    public List<Task> listTasksForUser(User user) {
        return repository.listTasksForUser(user.id());
    }
}
