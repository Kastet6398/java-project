package com.example.javaapp.models.repositories;

import com.example.javaapp.models.entities.Course;
import com.example.javaapp.models.entities.User;
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
    private static final String INSERT_TO_INVITED_USERS = "INSERT INTO main.course_invited_user (course_id, user_id) VALUES (:course_id, :user_id)";
    private static final String FINDCOURSE = "SELECT * FROM main.course WHERE title = :title;";
    private static final String FINDCOURSE_BY_ID = "SELECT * FROM main.course WHERE id = :id;";
    private static final String FIND_INVITED_USERS =
            "SELECT u.* FROM authentication.user u " +
            "JOIN main.course_invited_user cu ON u.id = cu.user_id " +
            "WHERE cu.course_id = :id";
    private static final String DELETE = "DELETE FROM main.course WHERE id = :id";
    private static final String LIST_COURSES_FOR_USER =
            "SELECT main.course.* FROM main.course_invited_user JOIN main.course ON main.course_invited_user.course_id = main.course.id JOIN authentication.user ON main.course_invited_user.user_id = authentication.user.id WHERE authentication.user.id = :id;";
    private final JdbcClient jdbcClient;

    public CourseRepository(JdbcClient jdbcClient) {
        this.jdbcClient = jdbcClient;
    }

    public Optional<Course> createCourse(Course course) {
        long affected = jdbcClient.sql(INSERT)
                .param("title", course.title())
                .param("description", course.description())
                .param("author", course.author())
                .update();

        Optional<Course> courseDb = findCourse(course.title(), false);
        Assert.isTrue(courseDb.isPresent(), "Could not add course.");

        for (User invitedUser : course.invitedUsers()) {
            long affected2 = jdbcClient.sql(INSERT_TO_INVITED_USERS)
                    .param("course_id", courseDb.get().id())
                    .param("user_id", invitedUser.id())
                    .update();
            Assert.isTrue(affected2 == 1, "Could not add some invited users.");
        }

        Assert.isTrue(affected == 1, "Could not add course.");

        return courseDb;
    }

    public Optional<Course> findCourse(String title, boolean isLoadInvitedUsers) {
        JdbcClient.ResultQuerySpec resultQuerySpec = jdbcClient.sql(FINDCOURSE)
                .param("title", title)
                .query();
        if (resultQuerySpec.listOfRows().isEmpty()) {
            return Optional.empty();
        }
        Map<String, Object> result = resultQuerySpec
                .singleRow();

        List<User> invitedUsers = new ArrayList<>();
        Course course = new Course(
                (String) result.get("title"),
                (String) result.get("description"),
                (long) result.get("author"),
                invitedUsers,
                (long) result.get("id"));
        if (isLoadInvitedUsers) {
            List<Map<String, Object>> result2 = jdbcClient.sql(FIND_INVITED_USERS)
                    .param("id", course.id())
                    .query()
                    .listOfRows();

            for (Map<String, Object> row : result2) {
                invitedUsers.add(new User(
                        (String) row.get("name"),
                        (String) row.get("email"),
                        (String) row.get("password"),
                        (String) row.get("phone"),
                        (long) row.get("id")));
            }
        }
        return Optional.of(course);
    }

    public Optional<Course> findCourse(String title) {
        return findCourse(title, true);
    }

    public List<Course> listCourses(long userId) {
        System.out.println(userId);
        List<Map<String, Object>> result = jdbcClient.sql(LIST_COURSES_FOR_USER)
                .param("id", userId)
                .query()
                .listOfRows();

        List<Course> courses = new ArrayList<>();
        for (Map<String, Object> row : result) {
            List<User> invitedUsers = new ArrayList<>();

            List<Map<String, Object>> result2 = jdbcClient.sql(FIND_INVITED_USERS)
                    .param("id", row.get("id"))
                    .query()
                    .listOfRows();

            for (Map<String, Object> row2 : result2) {
                invitedUsers.add(new User(
                        (String) row2.get("name"),
                        (String) row2.get("email"),
                        (String) row2.get("password"),
                        (String) row2.get("phone"),
                        (long) row2.get("id")));
            }
            courses.add(new Course(
                    (String) row.get("title"),
                    (String) row.get("description"),
                    (long) row.get("author"),
                    invitedUsers,
                    (long) row.get("id")));
        }
        return courses;
    }

    public boolean deleteTask(long id) {
        return jdbcClient.sql(DELETE)
                .param("id", id)
                .update() == 1;
    }

    public Optional<Course> findCourseById(long id) {
        try {
            return jdbcClient.sql(FINDCOURSE_BY_ID)
                    .param("id", id)
                    .query(Course.class)
                    .optional();
        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        }
    }
}
