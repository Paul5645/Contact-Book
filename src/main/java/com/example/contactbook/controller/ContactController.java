package com.example.contactbook.controller;

import com.example.contactbook.dto.ContactRequestDto;
import com.example.contactbook.dto.ContactResponseDto;
import com.example.contactbook.service.ContactService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Контроллер для обработки запросов, связанных с контактами.
 * <p>
 * Этот контроллер предоставляет методы для получения всех контактов, получения контакта по ID,
 * создания нового контакта, обновления существующего контакта и удаления контакта.
 * </p>
 * <p>
 * Все запросы маршрутизируются на путь "/contacts".
 * </p>
 */
@Slf4j
@RequiredArgsConstructor
@RequestMapping("/contacts")
@RestController
public class ContactController {

    private final ContactService contactService;

    /**
     * Получить список всех контактов.
     *
     * @return Список объектов {@link ContactResponseDto}, представляющих все контакты.
     */
    @GetMapping
    public List<ContactResponseDto> getAllContacts() {
        log.info("GET /contacts request - Fetching all contacts");
        return contactService.getAllContacts();
    }

    /**
     * Получить контакт по ID.
     *
     * @param id Идентификатор контакта, который требуется получить.
     * @return Объект {@link ContactResponseDto}, представляющий контакт с указанным ID.
     */
    @GetMapping("/{id}")
    public ContactResponseDto getContactById(@PathVariable Long id) {
        log.info("GET /contacts/{} request - Fetching contact", id);
        return contactService.getContactById(id);
    }

    /**
     * Создать новый контакт.
     *
     * @param contactRequestDto Объект {@link ContactRequestDto}, содержащий данные для создания контакта.
     * @return Ответ с созданным контактным объектом {@link ContactResponseDto}.
     */
    @PostMapping
    public ResponseEntity<ContactResponseDto> createContact(@RequestBody ContactRequestDto contactRequestDto) {
        log.info("POST /contacts request - Creating contact.");
        ContactResponseDto createdContact = contactService.createContact(contactRequestDto);
        return ResponseEntity.status(201).body(createdContact);
    }

    /**
     * Обновить или создать контакт.
     *
     * @param id Идентификатор контакта, который необходимо обновить.
     * @param contactRequestDto Объект {@link ContactRequestDto}, содержащий обновленные данные контакта.
     * @return Ответ с обновленным или созданным контактным объектом {@link ContactResponseDto}.
     */
    @PutMapping("/{id}")
    public ResponseEntity<ContactResponseDto> createOrUpdateContact(@PathVariable Long id, @RequestBody ContactRequestDto contactRequestDto) {
        log.info("PUT /contacts/{} - Updating contact.", id);
        ContactResponseDto updatedContact = contactService.saveOrUpdateContact(id, contactRequestDto);
        return ResponseEntity.ok(updatedContact);
    }

    /**
     * Удалить контакт по ID.
     *
     * @param id Идентификатор контакта, который необходимо удалить.
     * @return Ответ с кодом 204 (No Content), если контакт был успешно удален.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteContact(@PathVariable Long id) {
        log.info("DELETE /contacts/{} - Deleting contact", id);
        contactService.deleteContact(id);
        return ResponseEntity.noContent().build();
    }
}
