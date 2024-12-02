package com.example.contactbook;

import com.example.contactbook.model.Contact;
import com.example.contactbook.repository.ContactRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class ContactBookApplication {

    public static void main(String[] args) {
        SpringApplication.run(ContactBookApplication.class, args);
    }

    @Bean
    public CommandLineRunner loadData(ContactRepository contactRepository) {
        return args -> {
            // Создаем три контакта
            Contact contact1 = new Contact(null, "Alice Johnson", "1234567890", "alice@example.com");
            Contact contact2 = new Contact(null, "Bob Smith", "0987654321", "bob@example.com");
            Contact contact3 = new Contact(null, "Charlie Brown", "1122334455", "charlie@example.com");

            // Сохраняем контакты в базу данных
            contactRepository.save(contact1);
            contactRepository.save(contact2);
            contactRepository.save(contact3);
        };
    }

}
