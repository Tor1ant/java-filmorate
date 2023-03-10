package ru.yandex.practicum.filmorate.Exception;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
@Slf4j
public class ExceptionsHandler {
    @ExceptionHandler(ValidationException.class)
    public ResponseEntity<String> handleValidateException(final ValidationException validationException) {
        log.debug(validationException.getMessage());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(validationException.getMessage());
    }

    @ExceptionHandler(NotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public String notFoundExceptionHandler(final NotFoundException notFoundException) {
        log.debug(notFoundException.getMessage());
        return notFoundException.getMessage();
    }
}