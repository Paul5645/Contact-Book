package com.example.contactbook.controller;

import com.example.contactbook.dto.ContactRequestDto;
import com.example.contactbook.dto.ContactResponseDto;
import com.example.contactbook.service.ContactService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RequiredArgsConstructor
@RequestMapping("/contacts")
@RestController
public class ContactController {
    private final ContactService contactService;

    @GetMapping
    public List<ContactResponseDto> getAllContacts() {
        log.info("GET /contacts request - Fetching all contacts");
        return contactService.getAllContacts();
    }

    @GetMapping("/{id}")
    public ContactResponseDto getContactById(@PathVariable Long id) {
        log.info("GET /contacts/{} request - Fetching contact", id);
        return contactService.getContactById(id);
    }

    @PostMapping
    public ResponseEntity<ContactResponseDto> createContact(@RequestBody ContactRequestDto contactRequestDto) {
        log.info("POST /contacts request - Creating contact.");
        ContactResponseDto createdContact = contactService.createContact(contactRequestDto);
        return ResponseEntity.status(201).body(createdContact);
    }

    @PutMapping("/{id}")
    public ResponseEntity<ContactResponseDto> createOrUpdateContact(@PathVariable Long id, @RequestBody ContactRequestDto contactRequestDto) {
        log.info("PUT /contacts/{} - Updating contact.", id);
        ContactResponseDto updatedContact = contactService.saveOrUpdateContact(id, contactRequestDto);
        return ResponseEntity.ok(updatedContact);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteContact(@PathVariable Long id) {
        log.info("DELETE /contacts/{} - Deleting contact", id);
        contactService.deleteContact(id);
        return ResponseEntity.noContent().build();
    }
}
