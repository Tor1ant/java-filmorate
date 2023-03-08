package ru.yandex.practicum.filmorate.storage;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.Film;

@Component
public interface FilmStorage <T extends Film> {
    ResponseEntity<?> createFilm(T t);

    ResponseEntity<?> updateFilm(T t);

    ResponseEntity<?> deleteFilm(T t);
}
