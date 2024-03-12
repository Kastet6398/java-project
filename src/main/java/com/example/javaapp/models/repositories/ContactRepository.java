package com.example.javaapp.models.repositories;

import com.example.javaapp.models.entities.Contact;
import com.example.javaapp.models.entities.User;
import org.springframework.jdbc.core.simple.JdbcClient;
import org.springframework.stereotype.Repository;
import org.springframework.util.Assert;

import java.util.List;
import java.util.Map;
import java.util.Optional;

@Repository
public class ContactRepository {

    private static final String INSERT = "INSERT INTO main.contact (name, owner, contact_id) VALUES (:name, :owner, :contact_id)";
    private static final String DELETE = "DELETE FROM main.contact WHERE id = :id";
    private static final String FIND_CONTACT_BY_ID = "SELECT * FROM main.contact WHERE id = :id";
    private static final String FIND_CONTACT_BY_DATA = "SELECT * FROM main.contact WHERE \"owner\" = :owner AND contact_id = :contactId";
    private static final String FIND_CONTACTS_FOR_USER =
            "SELECT * FROM main.contact WHERE owner = :owner";
    private final JdbcClient jdbcClient;

    public ContactRepository(JdbcClient jdbcClient) {
        this.jdbcClient = jdbcClient;
    }

    public Optional<Contact> addContact(User owner, User contact, String name) {try {
        long affected = jdbcClient.sql(INSERT)
                .param("owner", owner.id())
                .param("contact_id", contact.id())
                .param("name", name)
                .update();

        Assert.isTrue(affected == 1, "Could not add contact.");
        return findContactByData(owner.id(), contact.id());
    }catch (Exception e) {
        e.printStackTrace();
        throw e;
    }
    }

    public boolean deleteContact(long id) {
        return jdbcClient.sql(DELETE)
                .param("id", id)
                .update() == 1;
    }

    public Optional<Contact> findContactById(long id) {
        return jdbcClient.sql(FIND_CONTACT_BY_ID)
                .param("id", id)
                .query(Contact.class).optional();
    }

    public Optional<Contact> findContactByData(long ownerId, long contactId) {
        return jdbcClient.sql(FIND_CONTACT_BY_DATA)
                .param("ownerId", ownerId)
                .param("contactId", contactId)
                .query(Contact.class).optional();
    }

    public List<Map<String, Object>> findContactsForUser(User owner) {
        return jdbcClient.sql(FIND_CONTACTS_FOR_USER)
                .param("owner", owner.id())
                .query()
                .listOfRows();
    }
}
