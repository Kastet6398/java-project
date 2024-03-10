package com.example.javaapp.models.repositories;

import com.example.javaapp.models.entities.User;
import org.springframework.jdbc.core.simple.JdbcClient;
import org.springframework.stereotype.Repository;
import org.springframework.util.Assert;

import java.util.Optional;

@Repository
public class UserRepository {

    private static final String INSERT = "INSERT INTO authentication.user (name, email, password, phone) VALUES(:name, :email, :password, :phone)";
    private static final String FIND_BY_EMAIL = "SELECT * FROM authentication.user WHERE email = :email";

    private final JdbcClient jdbcClient;

    public UserRepository(JdbcClient jdbcClient) {
        this.jdbcClient = jdbcClient;
    }

    public Optional<User> add(User user) {
        long affected = jdbcClient.sql(INSERT)
                .param("name", user.name())
                .param("email", user.email())
                .param("password", user.password())
                .param("phone", user.phone())
                .update();

        Assert.isTrue(affected == 1, "Could not add user.");
        return findByEmail(user.email());
    }

    public Optional<User> findByEmail(String email) {
        try {


            return jdbcClient.sql(FIND_BY_EMAIL)
                    .param("email", email)
                    .query(User.class)
                    .optional();
        }catch (Exception e) {
            e.printStackTrace();
            throw e;
        }
    }
}
