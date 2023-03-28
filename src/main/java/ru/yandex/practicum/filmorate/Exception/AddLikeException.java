package ru.yandex.practicum.filmorate.Exception;

import org.springframework.dao.DataAccessException;

public class AddLikeException extends DataAccessException {
    public AddLikeException(String message) {
        super(message);
    }
}
