package com.example.javaapp.models.repositories;

import com.example.javaapp.models.entities.Task;
import org.springframework.jdbc.core.simple.JdbcClient;
import org.springframework.stereotype.Repository;
import org.springframework.util.Assert;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Repository
public class TaskRepository {
    private static final String DELETE = "DELETE FROM main.task WHERE id = :id";
    private static final String INSERT = "INSERT INTO main.task (title, description, author, deadline, course_id) VALUES (:title, :description, :author, :deadline, :course_id)";
    private static final String FINDTASK = "SELECT * FROM main.task WHERE title = :title;";
    private static final String FINDTASK_BY_ID = "SELECT * FROM main.task WHERE id = :id AND course_id = :course_id;";
    private static final String LIST_TASKS_FOR_USER =
            """
                    SELECT task.*
                    FROM main.course_invited_user
                    JOIN main.course ON main.course_invited_user.course_id = main.course.id
                    JOIN main.task ON main.course.id = main.task.course_id
                    JOIN authentication.user ON main.course_invited_user.user_id = authentication.user.id
                    WHERE authentication.user.id = :id;
                    """;

    private final JdbcClient jdbcClient;

    public TaskRepository(JdbcClient jdbcClient) {
        this.jdbcClient = jdbcClient;
    }

    public Optional<Task> createTask(Task task) {

        long affected = jdbcClient.sql(INSERT)
                .param("title", task.title())
                .param("description", task.description())
                .param("author", task.author())
                .param("deadline", task.deadline())
                .param("course_id", task.courseId())
                .update();

        Assert.isTrue(affected == 1, "Could not add task.");
        return findTask(task.title(), task.courseId());
    }

    public Optional<Task> findTask(String title, long courseId) {
        return jdbcClient.sql(FINDTASK)
                .param("title", title)
                .param("course_id", courseId)
                .query(Task.class)
                .optional();
    }

    public boolean deleteTask(long id) {
        return jdbcClient.sql(DELETE)
                .param("id", id)
                .update() == 1;
    }

    public Optional<Task> findTaskById(long id) {
        JdbcClient.ResultQuerySpec resultQuerySpec = jdbcClient.sql(FINDTASK_BY_ID)
                .param("id", id)
                .query();
        if (resultQuerySpec.listOfRows().isEmpty()) {
            return Optional.empty();
        }
        return Optional.of(new Task(
                (String) resultQuerySpec.singleRow().get("title"),
                (String) resultQuerySpec.singleRow().get("description"),
                (long) resultQuerySpec.singleRow().get("author"),
                ((Timestamp) resultQuerySpec.singleRow().get("deadline")).toLocalDateTime(),
                (long) resultQuerySpec.singleRow().get("id"),
                (long) resultQuerySpec.singleRow().get("course_id")));
    }

    public List<Task> listTasksForUser(long id) {
        try {
            List<Map<String, Object>> result = jdbcClient.sql(LIST_TASKS_FOR_USER)
                    .param("id", id)
                    .query()
                    .listOfRows();

            List<Task> tasks = new ArrayList<>();
            for (Map<String, Object> row : result) {
                System.out.println(row);
                tasks.add(new Task(
                        (String) row.get("title"),
                        (String) row.get("description"),
                        (long) row.get("author"),
                        ((Timestamp) row.get("deadline")).toLocalDateTime(),
                        (long) row.get("id"),
                        (long) row.get("course_id")));
            }
            return tasks;
        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        }
    }
}