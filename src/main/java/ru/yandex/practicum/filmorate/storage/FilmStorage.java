package ru.yandex.practicum.filmorate.storage;

import org.springframework.http.ResponseEntity;
import ru.yandex.practicum.filmorate.model.Film;

public interface FilmStorage {
    ResponseEntity<?> createFilm(Film film);

    ResponseEntity<?> updateFilm(Film film);

    ResponseEntity<?> deleteFilm(Film film);
}
