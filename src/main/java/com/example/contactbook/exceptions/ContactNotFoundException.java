package com.example.contactbook.exceptions;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class ContactNotFoundException extends RuntimeException {
    public ContactNotFoundException(String message) {
        super(message);
    }
}
