package com.example.contactbook.service;

import com.example.contactbook.dto.ContactDto;
import com.example.contactbook.exceptions.ContactNotFoundException;
import com.example.contactbook.model.Contact;
import com.example.contactbook.repository.ContactRepository;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validator;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;


@RequiredArgsConstructor
@Service
public class ContactService {
    private final ContactRepository contactRepository;
    private final Validator validator;
    private final ModelMapper mapper;

    public ContactDto getContactById(Long id) {
        Contact contact = contactRepository.findById(id)
                .orElseThrow(() -> new ContactNotFoundException("Contact with ID " + id + " not found."));
        return mapper.map(contact, ContactDto.class);
    }

    public List<ContactDto> getAllContacts(){
        return contactRepository.findAll()
                .stream()
                .map(contact -> mapper.map(contact,ContactDto.class))
                .collect(Collectors.toList());
    }

    public Contact createContact(ContactDto contactDto){
        Contact contact = mapper.map(contactDto, Contact.class);
        validateContact(contact);
        return contactRepository.save(contact);
    }

    public Contact updateOrCreateContact(Long id, ContactDto contactDto) {
        Contact contact = mapper.map(contactDto, Contact.class);
        validateContact(contact);
        contact.setId(id);
        return contactRepository.save(contact);
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
