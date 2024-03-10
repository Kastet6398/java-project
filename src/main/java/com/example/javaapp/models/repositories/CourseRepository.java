package com.example.javaapp.models.repositories;

import com.example.javaapp.models.entities.Course;
import org.springframework.jdbc.core.simple.JdbcClient;
import org.springframework.stereotype.Repository;
import org.springframework.util.Assert;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Repository
public class CourseRepository {

    private static final String INSERT = "INSERT INTO main.course (title, description, author) VALUES (:title, :description, :author)";
    private static final String FINDCOURSE = "SELECT * FROM main.course WHERE title = :title";
    private static final String FINDCOURSE_BY_ID = "SELECT * FROM main.course WHERE id = :id";
    private static final String DELETE = "DELETE FROM main.course WHERE id = :id";
    private static final String LIST_COURSES_FOR_USER = """
            SELECT courses.*
            FROM courses
            JOIN course_user ON courses.id = course_user.course_id
            WHERE course_user.user_id = :id;
            """;
    private final JdbcClient jdbcClient;

    public CourseRepository(JdbcClient jdbcClient) {
        this.jdbcClient = jdbcClient;
    }

    public Optional<Course> createCourse(Course course){

        long affected = jdbcClient.sql(INSERT)
                .param("title", course.title())
                .param("description", course.description())
                .param("author", course.author())
                .update();

        Assert.isTrue(affected == 1, "Could not add course.");

        return findCourse(course.title());
    }

    public Optional<Course> findCourse(String title) {
        return jdbcClient.sql(FINDCOURSE)
                .param("title", title)
                .query(Course.class)
                .optional();
    }

    public List<Course> listCourses(long userId) {
        List<Map<String, Object>> result = jdbcClient.sql(LIST_COURSES_FOR_USER)
                .param("id", userId)
                .query()
                .listOfRows();

        List<Course> courses = new ArrayList<>();
        for (Map<String, Object> row : result) {
            courses.add(new Course(
                    (String) row.get("title"),
                    (String) row.get("description"),
                    (long) row.get("author"),
                    (long) row.get("id")));
        }
        return courses;
    }

    public boolean deleteTask(long id) {
        return jdbcClient.sql(DELETE)
                .param("id", id)
                .update() == 1;
    }

    public Optional<Course> findTaskById(long id) {
        return jdbcClient.sql(FINDCOURSE_BY_ID)
                .param("id", id)
                .query(Course.class)
                .optional();
    }
}