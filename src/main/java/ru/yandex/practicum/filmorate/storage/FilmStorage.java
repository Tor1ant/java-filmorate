package ru.yandex.practicum.filmorate.storage;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.Film;

import java.util.Map;

@Component
public interface FilmStorage {
    ResponseEntity<?> createFilm(Film film);

    ResponseEntity<?> updateFilm(Film film);

    ResponseEntity<?> deleteFilm(Film film);

    Map<Integer, Film> getFilms();
}
