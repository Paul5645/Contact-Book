package com.example.contactbook.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * Модель, представляющая контакт в базе данных.
 * Содержит информацию о контакте, включая имя, номер телефона и email.
 */
@Entity
@Table(name = "contacts")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Contact {

    /**
     * Уникальный идентификатор контакта.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * Имя контакта. Не может быть пустым.
     */
    @NotBlank(message = "Name must not be blank")
    private String name;

    /**
     * Номер телефона контакта. Не может быть пустым.
     */
    @NotBlank(message = "Phone number must not be blank")
    private String phoneNumber;

    /**
     * Электронная почта контакта. Должна иметь корректный формат.
     */
    @Email(message = "Invalid email format")
    private String email;
}
