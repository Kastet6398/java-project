package com.example.javaapp.models.repositories;

import com.example.javaapp.models.entities.Task;
import org.springframework.jdbc.core.simple.JdbcClient;
import org.springframework.util.Assert;

import java.util.Map;

public class TaskRepository {

    private static final String CREATE = "CREATE TABLE IF NOT EXISTS course_tasks (title, description, author, createdAt, deadline, task_id, course_id)";
    private static final String INSERT = "INSERT INTO course_tasks (title, description, author, createdAt, deadline, task_id, course_id) VALUES (:title, :description, :author, :createdAt, :deadline, :task_id, :course_id)";
    private static final String UPDATE = "UPDATE course_tasks SET someInfo =''";
    private static final String DELETE = " DELETE FROM course_tasks WHERE ";
    private static final String FINDTASK = "SELECT * FROM course_tasks WHERE title = :title";
    private final JdbcClient jdbcClient;

    public TaskRepository(JdbcClient jdbcClient) {
        this.jdbcClient = jdbcClient;
    }

    public Task createTask(Task task){

        long affected = jdbcClient.sql(INSERT)
                .param("title", task.title())
                .param("description", task.description())
                .param("author", task.author())
                .param("createdAt", task.createdAt())
                .param("deadline", task.deadline())
                .param("task_id", task.task_id())
                .param("course_id", task.course_id())
                .update();

        Assert.isTrue(affected == 1, "Could not add task.");
        Map<String, Object> result = jdbcClient.sql(FINDTASK)
                .param("title", task.title())
                .query()
                .singleRow();

        return new Task(
                (String) result.get("title"),
                (String) result.get("description"),
                (String) result.get("author"),
                (String) result.get("createdAt"),
                (String) result.get("deadline"),
                (String) result.get("task_id"),
                (String) result.get("course_id"));
    }

    public Task updateTask(Task task){
        return null;
    }

    public void delete(String taskname){}
}