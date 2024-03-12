package com.example.javaapp.models.services;

import com.example.javaapp.exceptions.DuplicateException;
import com.example.javaapp.models.entities.User;
import com.example.javaapp.models.repositories.ContactRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.example.javaapp.models.entities.Contact;

import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
@Transactional(readOnly = true)
public class ContactService {
    private final ContactRepository repository;
    private final UserService userService;

    public ContactService(ContactRepository repository, UserService userService) {
        this.repository = repository;
        this.userService = userService;
    }

    @Transactional
    public boolean deleteContact(long id) {
        return repository.deleteContact(id);
    }

    public Optional<Contact> findContactById(long id) {
        return repository.findContactById(id);
    }

    public List<Map<String, Object>> findContactsForUser(User owner) {
        return repository.findContactsForUser(owner);
    }

    public Optional<Contact> addContact(Contact contact) {

        Optional<Contact> existingContactId = repository.addContact(contact.owner(), contact.contact(), contact.name());

        return existingContactId;
    }
}
