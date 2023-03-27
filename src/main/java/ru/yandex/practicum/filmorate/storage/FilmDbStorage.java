package ru.yandex.practicum.filmorate.storage;

import org.springframework.http.ResponseEntity;
import ru.yandex.practicum.filmorate.model.Film;

import java.util.Map;

public class FilmDbStorage implements FilmStorage{
    @Override
    public ResponseEntity<Film> createFilm(Film film) {
        return null;
    }

    @Override
    public ResponseEntity<Film> updateFilm(Film film) {
        return null;
    }

    @Override
    public ResponseEntity<Film> deleteFilm(Film film) {
        return null;
    }

    @Override
    public Map<Integer, Film> getFilms() {
        return null;
    }
}
