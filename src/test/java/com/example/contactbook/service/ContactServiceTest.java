package com.example.contactbook.service;

import com.example.contactbook.dto.ContactRequestDto;
import com.example.contactbook.dto.ContactResponseDto;
import com.example.contactbook.exceptions.ContactNotFoundException;
import com.example.contactbook.model.Contact;
import com.example.contactbook.repository.ContactRepository;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.modelmapper.ModelMapper;

import java.util.List;
import java.util.Optional;
import java.util.Set;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

class ContactServiceTest {

    @Mock
    private ContactRepository contactRepository;

    @Mock
    private Validator validator;

    @Mock
    private ModelMapper modelMapper;

    @InjectMocks
    private ContactService contactService;

    private Contact contact;
    private ContactRequestDto contactRequestDto;
    private ContactResponseDto contactResponseDto;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();

        contact = new Contact();
        contact.setId(1L);
        contact.setName("John Doe");
        contact.setPhoneNumber("1234567890");
        contact.setEmail("john.doe@example.com");

        contactRequestDto = new ContactRequestDto();
        contactRequestDto.setName("John Doe");
        contactRequestDto.setPhoneNumber("1234567890");
        contactRequestDto.setEmail("john.doe@example.com");

        contactResponseDto = new ContactResponseDto();
        contactResponseDto.setId(1L);
        contactResponseDto.setName("John Doe");
        contactResponseDto.setPhoneNumber("1234567890");
        contactResponseDto.setEmail("john.doe@example.com");
    }

    @Test
    void getContactById_ShouldReturnContactDto_WhenContactExists() {
        when(contactRepository.findById(1L)).thenReturn(Optional.of(contact));
        when(modelMapper.map(contact, ContactResponseDto.class)).thenReturn(contactResponseDto);

        ContactResponseDto result = contactService.getContactById(1L);

        assertEquals(contactResponseDto, result);
        verify(contactRepository).findById(1L);
        verify(modelMapper).map(contact, ContactResponseDto.class);
    }

    @Test
    void getContactById_ShouldThrowContactNotFoundException_WhenContactDoesNotExist() {
        when(contactRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(ContactNotFoundException.class, () -> contactService.getContactById(1L));
        verify(contactRepository).findById(1L);
    }

    @Test
    void getAllContacts_ShouldReturnListOfContactDtos() {
        when(contactRepository.findAll()).thenReturn(List.of(contact));
        when(modelMapper.map(any(Contact.class), eq(ContactResponseDto.class))).thenReturn(contactResponseDto);

        List<ContactResponseDto> result = contactService.getAllContacts();

        assertFalse(result.isEmpty());
        assertEquals(1, result.size());
        verify(contactRepository).findAll();
        verify(modelMapper).map(any(Contact.class), eq(ContactResponseDto.class));
    }

    @Test
    void createContact_ShouldReturnCreatedContactDto() {
        when(modelMapper.map(contactRequestDto, Contact.class)).thenReturn(contact);
        when(contactRepository.save(any(Contact.class))).thenReturn(contact);
        when(modelMapper.map(contact, ContactResponseDto.class)).thenReturn(contactResponseDto);

        ContactResponseDto result = contactService.createContact(contactRequestDto);

        assertEquals(contactResponseDto, result);
        verify(modelMapper).map(contactRequestDto, Contact.class);
        verify(contactRepository).save(any(Contact.class));
        verify(modelMapper).map(contact, ContactResponseDto.class);
    }

    @Test
    void saveOrUpdateContact_ShouldReturnUpdatedContactDto() {
        when(modelMapper.map(contactRequestDto, Contact.class)).thenReturn(contact);
        when(contactRepository.save(any(Contact.class))).thenReturn(contact);
        when(modelMapper.map(contact, ContactResponseDto.class)).thenReturn(contactResponseDto);

        ContactResponseDto result = contactService.saveOrUpdateContact(1L, contactRequestDto);

        assertEquals(contactResponseDto, result);
        verify(modelMapper).map(contactRequestDto, Contact.class);
        verify(contactRepository).save(any(Contact.class));
        verify(modelMapper).map(contact, ContactResponseDto.class);
    }

    @Test
    void deleteContact_ShouldDeleteContact_WhenExists() {
        when(contactRepository.existsById(1L)).thenReturn(true);

        contactService.deleteContact(1L);

        verify(contactRepository).existsById(1L);
        verify(contactRepository).deleteById(1L);
    }

    @Test
    void deleteContact_ShouldThrowContactNotFoundException_WhenContactDoesNotExist() {
        when(contactRepository.existsById(1L)).thenReturn(false);

        assertThrows(ContactNotFoundException.class, () -> contactService.deleteContact(1L));
        verify(contactRepository).existsById(1L);
        verify(contactRepository, never()).deleteById(1L);
    }

    @Test
    void validateContact_ShouldPass_WhenContactIsValid() {
        Set<ConstraintViolation<Contact>> violations = validator.validate(contact);

        assertTrue(violations.isEmpty(), "Expected no validation violations");
    }

    @Test
    void validateContact_ShouldFail_WhenNameIsBlank() {
        contact.setName("");

        Set<ConstraintViolation<Contact>> violations = validator.validate(contact);

        assertFalse(violations.isEmpty(), "Expected validation violations");
        assertTrue(violations.stream().anyMatch(v -> v.getMessage().equals("Name must not be blank")),
                "Expected violation for blank name");
    }

    @Test
    void validateContact_ShouldFail_WhenPhoneNumberIsBlank() {
        contact.setPhoneNumber("");

        Set<ConstraintViolation<Contact>> violations = validator.validate(contact);

        assertFalse(violations.isEmpty(), "Expected validation violations");
        assertTrue(violations.stream().anyMatch(v -> v.getMessage().equals("Phone number must not be blank")),
                "Expected violation for blank phone number");
    }

    @Test
    void validateContact_ShouldFail_WhenEmailIsInvalid() {
        contact.setEmail("invalid-email");

        Set<ConstraintViolation<Contact>> violations = validator.validate(contact);

        assertFalse(violations.isEmpty(), "Expected validation violations");
        assertTrue(violations.stream().anyMatch(v -> v.getMessage().equals("Invalid email format")),
                "Expected violation for invalid email format");
    }
}
