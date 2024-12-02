package com.example.contactbook.exceptions;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

import java.util.Date;

/**
 * Класс для представления ошибок, возникающих в приложении.
 * Используется для передачи информации об ошибке в клиентском ответе.
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
@Data
public class AppError {
    /**
     * HTTP-статус ошибки.
     */
    private int status;

    /**
     * Сообщение об ошибке.
     */
    private String message;

    /**
     * Временная метка, указывающая время возникновения ошибки.
     */
    private Date timestamp;

    /**
     * Конструктор для создания объекта ошибки.
     *
     * @param status  HTTP-статус ошибки.
     * @param message Сообщение об ошибке.
     */
    public AppError(int status, String message) {
        this.status = status;
        this.message = message;
        this.timestamp = new Date();
    }
}
