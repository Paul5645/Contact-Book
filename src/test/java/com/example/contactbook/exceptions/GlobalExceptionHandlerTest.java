package com.example.contactbook.exceptions;

import com.example.contactbook.controller.ContactController;
import com.example.contactbook.dto.ContactRequestDto;
import com.example.contactbook.service.ContactService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(MockitoExtension.class)
class GlobalExceptionHandlerTest {

    private MockMvc mockMvc;

    @Mock
    private ContactService contactService;

    @InjectMocks
    private ContactController contactController;

    @InjectMocks
    private GlobalExceptionHandler globalExceptionHandler;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(contactController)
                .setControllerAdvice(globalExceptionHandler)
                .build();
    }

    @Test
    void handleContactNotFoundException_ShouldReturnNotFound() throws Exception {
        when(contactService.getContactById(1L)).thenThrow(new ContactNotFoundException("Contact not found"));

        mockMvc.perform(get("/contacts/1"))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.status").value(HttpStatus.NOT_FOUND.value()))
                .andExpect(jsonPath("$.message").value("Contact not found"));
    }

    @Test
    void handleIllegalArgumentException_ShouldReturnBadRequest() throws Exception {
        when(contactService.createContact(any(ContactRequestDto.class)))
                .thenThrow(new IllegalArgumentException("Invalid contact data"));

        mockMvc.perform(post("/contacts")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{ \"name\": \"\", \"phoneNumber\": \"1234567890\", \"email\": \"\" }"))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.status").value(HttpStatus.BAD_REQUEST.value()))
                .andExpect(jsonPath("$.message").value("Invalid contact data"));
    }

    @Test
    void handleGenericException_ShouldReturnInternalServerError() throws Exception {
        when(contactService.getContactById(1L)).thenThrow(new RuntimeException("Unexpected error"));

        mockMvc.perform(get("/contacts/1"))
                .andExpect(status().isInternalServerError())
                .andExpect(jsonPath("$.status").value(HttpStatus.INTERNAL_SERVER_ERROR.value()))
                .andExpect(jsonPath("$.message").value("An unexpected error occurred."));
    }
}
