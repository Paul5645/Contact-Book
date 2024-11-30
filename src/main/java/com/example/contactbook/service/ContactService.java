package com.example.contactbook.service;

import com.example.contactbook.exceptions.ContactNotFoundException;
import com.example.contactbook.model.Contact;
import com.example.contactbook.repository.ContactRepository;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validator;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.Set;


@RequiredArgsConstructor
@Service
public class ContactService {
    private final ContactRepository contactRepository;
    private final Validator validator;

    public Optional<Contact> getContactById(Long id){
        return contactRepository.findById(id);
    }

    public List<Contact> getAllContacts(){
        return contactRepository.findAll();
    }

    public Contact createContact(Contact contact){
        validateContact(contact);
        return contactRepository.save(contact);
    }

    public Contact updateContact(Long id, Contact contact) {
        validateContact(contact);
        if (contactRepository.existsById(id)) {
            contact.setId(id);
            return contactRepository.save(contact);
        }
        throw new ContactNotFoundException("Contact with ID " + id + " does not exist.");
    }

    public void deleteContact(Long id) {
        if (!contactRepository.existsById(id)) {
            throw new ContactNotFoundException("Contact with ID " + id + " does not exist.");
        }
        contactRepository.deleteById(id);
    }

    private void validateContact(Contact contact) {
        Set<ConstraintViolation<Contact>> violations = validator.validate(contact);
        if (!violations.isEmpty()) {
            StringBuilder errorMessage = new StringBuilder("Validation failed for Contact: ");
            for (ConstraintViolation<Contact> violation : violations) {
                errorMessage.append(violation.getMessage()).append("; ");
            }
            throw new IllegalArgumentException(errorMessage.toString());
        }
    }
}
