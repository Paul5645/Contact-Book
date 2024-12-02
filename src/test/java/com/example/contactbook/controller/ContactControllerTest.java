package com.example.contactbook.controller;

import com.example.contactbook.dto.ContactRequestDto;
import com.example.contactbook.dto.ContactResponseDto;
import com.example.contactbook.service.ContactService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

class ContactControllerTest {

    @Mock
    private ContactService contactService;

    @InjectMocks
    private ContactController contactController;

    private MockMvc mockMvc;

    private ContactRequestDto contactRequestDto;
    private ContactResponseDto contactResponseDto;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(contactController).build();

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
    void getAllContacts_ShouldReturnListOfContacts() throws Exception {
        when(contactService.getAllContacts()).thenReturn(List.of(contactResponseDto));

        mockMvc.perform(get("/contacts"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$[0].id").value(1))
                .andExpect(jsonPath("$[0].name").value("John Doe"))
                .andExpect(jsonPath("$[0].phoneNumber").value("1234567890"))
                .andExpect(jsonPath("$[0].email").value("john.doe@example.com"));

        verify(contactService).getAllContacts();
    }

    @Test
    void getContactById_ShouldReturnContact_WhenContactExists() throws Exception {
        when(contactService.getContactById(1L)).thenReturn(contactResponseDto);

        mockMvc.perform(get("/contacts/1"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.name").value("John Doe"))
                .andExpect(jsonPath("$.phoneNumber").value("1234567890"))
                .andExpect(jsonPath("$.email").value("john.doe@example.com"));

        verify(contactService).getContactById(1L);
    }

    @Test
    void createContact_ShouldReturnCreatedContact() throws Exception {
        when(contactService.createContact(any(ContactRequestDto.class))).thenReturn(contactResponseDto);

        mockMvc.perform(post("/contacts")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{ \"name\": \"John Doe\", \"phoneNumber\": \"1234567890\", \"email\": \"john.doe@example.com\" }"))
                .andExpect(status().isCreated())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.name").value("John Doe"))
                .andExpect(jsonPath("$.phoneNumber").value("1234567890"))
                .andExpect(jsonPath("$.email").value("john.doe@example.com"));

        verify(contactService).createContact(any(ContactRequestDto.class));
    }

    @Test
    void createOrUpdateContact_ShouldReturnUpdatedContact() throws Exception {
        when(contactService.saveOrUpdateContact(eq(1L), any(ContactRequestDto.class))).thenReturn(contactResponseDto);

        mockMvc.perform(put("/contacts/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{ \"name\": \"John Doe\", \"phoneNumber\": \"1234567890\", \"email\": \"john.doe@example.com\" }"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.name").value("John Doe"))
                .andExpect(jsonPath("$.phoneNumber").value("1234567890"))
                .andExpect(jsonPath("$.email").value("john.doe@example.com"));

        verify(contactService).saveOrUpdateContact(eq(1L), any(ContactRequestDto.class));
    }

    @Test
    void deleteContact_ShouldReturnNoContent_WhenContactExists() throws Exception {
        doNothing().when(contactService).deleteContact(1L);

        mockMvc.perform(delete("/contacts/1"))
                .andExpect(status().isNoContent());

        verify(contactService).deleteContact(1L);
    }
}
