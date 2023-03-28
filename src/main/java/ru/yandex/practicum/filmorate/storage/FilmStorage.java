package ru.yandex.practicum.filmorate.storage;

import org.springframework.http.ResponseEntity;
import ru.yandex.practicum.filmorate.model.Film;

import java.util.List;
import java.util.Map;


public interface FilmStorage {
    ResponseEntity<Film> createFilm(Film film);

    ResponseEntity<Film> updateFilm(Film film);

    ResponseEntity<Film> deleteFilm(Film film);

    Map<Integer, Film> getFilms();

    Film getFilm(int filmId);

    void addLikeToFilm(int filmId, int userId);

    void removeLikeFromFilm(Integer id, Integer userId);

    List<Film> mostLikedFilms(Integer count);
}
