package com.example.contactbook.service;

import com.example.contactbook.dto.ContactRequestDto;
import com.example.contactbook.dto.ContactResponseDto;
import com.example.contactbook.exceptions.ContactNotFoundException;
import com.example.contactbook.model.Contact;
import com.example.contactbook.repository.ContactRepository;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validator;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * Сервис для работы с контактами.
 * Содержит методы для получения, создания, обновления, удаления контактов и их валидации.
 */
@Slf4j
@RequiredArgsConstructor
@Service
public class ContactService {

    private final ContactRepository contactRepository;
    private final Validator validator;
    private final ModelMapper mapper;

    /**
     * Получение контакта по его идентификатору.
     *
     * @param id Идентификатор контакта
     * @return Объект {@link ContactResponseDto} с информацией о контакте
     * @throws ContactNotFoundException Если контакт с таким идентификатором не найден
     */
    public ContactResponseDto getContactById(Long id) {
        log.info("Fetching contact by ID {}", id);
        Contact contact = contactRepository.findById(id)
                .orElseThrow(() -> new ContactNotFoundException("Contact with ID " + id + " not found."));
        log.info("Contact with ID {} fetched successfully", id);
        return mapper.map(contact, ContactResponseDto.class);
    }

    /**
     * Получение всех контактов.
     *
     * @return Список объектов {@link ContactResponseDto} с информацией о всех контактах
     */
    public List<ContactResponseDto> getAllContacts() {
        log.info("Fetching all contacts");
        List<ContactResponseDto> contacts = contactRepository.findAll()
                .stream()
                .map(contact -> mapper.map(contact, ContactResponseDto.class))
                .collect(Collectors.toList());
        log.info("Fetched {} contacts", contacts.size());
        return contacts;
    }

    /**
     * Создание нового контакта.
     *
     * @param contactRequestDto Объект {@link ContactRequestDto} с информацией о новом контакте
     * @return Объект {@link ContactResponseDto} с данными созданного контакта
     */
    public ContactResponseDto createContact(ContactRequestDto contactRequestDto) {
        log.info("Creating new contact.");
        Contact contact = mapper.map(contactRequestDto, Contact.class);
        validateContact(contact);
        Contact savedContact = contactRepository.save(contact);
        log.info("Contact created with ID {}", savedContact.getId());
        return mapper.map(savedContact, ContactResponseDto.class);
    }

    /**
     * Обновление существующего контакта или создание нового, если контакт с таким идентификатором не найден.
     *
     * @param id Идентификатор контакта
     * @param contactRequestDto Объект {@link ContactRequestDto} с данными контакта
     * @return Объект {@link ContactResponseDto} с данными обновленного или созданного контакта
     */
    public ContactResponseDto saveOrUpdateContact(Long id, ContactRequestDto contactRequestDto) {
        log.info("Updating or creating contact with ID {}.", id);
        Contact contact = mapper.map(contactRequestDto, Contact.class);
        validateContact(contact);
        contact.setId(id);
        Contact savedContact = contactRepository.save(contact);
        log.info("Contact with ID {} saved or updated successfully", id);
        return mapper.map(savedContact, ContactResponseDto.class);
    }

    /**
     * Удаление контакта по идентификатору.
     *
     * @param id Идентификатор контакта
     * @throws ContactNotFoundException Если контакт с таким идентификатором не найден
     */
    public void deleteContact(Long id) {
        log.info("Deleting contact with ID {}", id);
        if (!contactRepository.existsById(id)) {
            throw new ContactNotFoundException("Contact with ID " + id + " does not exist.");
        }
        contactRepository.deleteById(id);
        log.info("Contact with ID {} deleted successfully", id);
    }

    /**
     * Валидация контакта перед сохранением.
     *
     * @param contact Объект {@link Contact} для валидации
     * @throws IllegalArgumentException Если валидация не пройдена
     */
    private void validateContact(Contact contact) {
        log.info("Validating contact...");
        Set<ConstraintViolation<Contact>> violations = validator.validate(contact);
        if (!violations.isEmpty()) {
            StringBuilder errorMessage = new StringBuilder("Validation failed for Contact: ");
            for (ConstraintViolation<Contact> violation : violations) {
                errorMessage.append(violation.getMessage()).append("; ");
            }
            throw new IllegalArgumentException(errorMessage.toString());
        }
        log.info("Validation passed.");
    }
}
