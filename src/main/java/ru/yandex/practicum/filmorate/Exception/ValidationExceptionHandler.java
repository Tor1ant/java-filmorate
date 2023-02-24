package ru.yandex.practicum.filmorate.Exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class ValidationExceptionHandler {
    @ExceptionHandler(ValidationException.class)
    public static ResponseEntity<String> handleValidateException(ValidationException validationException) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(validationException.getMessage());
    }
}