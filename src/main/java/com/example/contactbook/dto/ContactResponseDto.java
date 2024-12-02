package com.example.contactbook.dto;

import lombok.Data;

@Data
public class ContactResponseDto {
    private Long id;

    private String name;

    private String phoneNumber;

    private String email;
}
