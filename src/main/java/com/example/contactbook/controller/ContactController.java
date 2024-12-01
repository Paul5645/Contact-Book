package com.example.contactbook.controller;


import com.example.contactbook.dto.ContactDto;
import com.example.contactbook.model.Contact;
import com.example.contactbook.service.ContactService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequiredArgsConstructor
@RequestMapping("/contacts")
@RestController
public class ContactController {
    private final ContactService contactService;

    @GetMapping
    public List<ContactDto> getAllContacts(){
        return contactService.getAllContacts();
    }


    @GetMapping("/{id}")
    public ContactDto getContactById(@PathVariable Long id) {
        return contactService.getContactById(id);
    }

    @PostMapping
    public ResponseEntity<Contact> createContact(@RequestBody ContactDto contactDto) {
        Contact createdContact = contactService.createContact(contactDto);
        return ResponseEntity.status(201).body(createdContact);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Contact> updateContact(@PathVariable Long id, @RequestBody ContactDto contactDto) {
        Contact updatedContact = contactService.updateOrCreateContact(id, contactDto);
        return ResponseEntity.ok(updatedContact);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteContact(@PathVariable Long id) {
        contactService.deleteContact(id);
        return ResponseEntity.noContent().build();
    }
}
