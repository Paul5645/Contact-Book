package com.example.contactbook.dto;

import lombok.Data;

@Data
public class ContactRequestDto {

    private String name;

    private String phoneNumber;

    private String email;
}
