package com.example.contactbook.exceptions;

import lombok.RequiredArgsConstructor;

/**
 * Исключение, указывающее, что запрашиваемый контакт не найден.
 */
@RequiredArgsConstructor
public class ContactNotFoundException extends RuntimeException {
    /**
     * Создает исключение с указанным сообщением.
     *
     * @param message Сообщение об ошибке.
     */
    public ContactNotFoundException(String message) {
        super(message);
    }
}
