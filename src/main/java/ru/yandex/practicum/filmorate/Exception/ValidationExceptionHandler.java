package ru.yandex.practicum.filmorate.Exception;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
@Slf4j
public class ValidationExceptionHandler {
    @ExceptionHandler(ValidationException.class)
    public static ResponseEntity<String> handleValidateException(ValidationException validationException) {
        log.debug(validationException.getMessage());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(validationException.getMessage());
    }
}