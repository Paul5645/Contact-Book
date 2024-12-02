package com.example.contactbook.exceptions;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

/**
 * Глобальный обработчик исключений для обработки ошибок в приложении.
 */
@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

    /**
     * Обрабатывает исключение {@link ContactNotFoundException}, возникающее при попытке доступа к несуществующему контакту.
     *
     * @param ex Исключение {@link ContactNotFoundException}.
     * @return Ответ с информацией об ошибке и статусом 404 (NOT FOUND).
     */
    @ExceptionHandler(ContactNotFoundException.class)
    public ResponseEntity<AppError> handleContactNotFoundException(ContactNotFoundException ex) {
        log.error("Contact not found: {}", ex.getMessage());
        AppError error = new AppError(HttpStatus.NOT_FOUND.value(), ex.getMessage());
        return new ResponseEntity<>(error, HttpStatus.NOT_FOUND);
    }

    /**
     * Обрабатывает исключение {@link IllegalArgumentException}, возникающее при передаче недопустимого аргумента.
     *
     * @param ex Исключение {@link IllegalArgumentException}.
     * @return Ответ с информацией об ошибке и статусом 400 (BAD REQUEST).
     */
    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<AppError> handleIllegalArgumentException(IllegalArgumentException ex) {
        log.error("Illegal argument: {}", ex.getMessage());
        AppError error = new AppError(HttpStatus.BAD_REQUEST.value(), ex.getMessage());
        return new ResponseEntity<>(error, HttpStatus.BAD_REQUEST);
    }

    /**
     * Обрабатывает любые неожиданные исключения, возникающие во время выполнения приложения.
     *
     * @param ex Исключение {@link Exception}.
     * @return Ответ с информацией об ошибке и статусом 500 (INTERNAL SERVER ERROR).
     */
    @ExceptionHandler(Exception.class)
    public ResponseEntity<AppError> handleGenericException(Exception ex) {
        log.error("Unexpected error: {}", ex.getMessage(), ex);
        AppError error = new AppError(HttpStatus.INTERNAL_SERVER_ERROR.value(), "An unexpected error occurred.");
        return new ResponseEntity<>(error, HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
