package ru.yandex.practicum.filmorate.storage;

import org.springframework.http.ResponseEntity;
import ru.yandex.practicum.filmorate.model.Film;

import java.util.Map;


public interface FilmStorage {
    ResponseEntity<Film> createFilm(Film film);

    ResponseEntity<Film> updateFilm(Film film);

    ResponseEntity<Film> deleteFilm(Film film);

    Map<Integer, Film> getFilms();
}
