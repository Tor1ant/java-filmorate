package ru.yandex.practicum.filmorate.storage;

import ru.yandex.practicum.filmorate.model.Film;

import java.util.List;
import java.util.Map;

public interface FilmStorage {
    Film createFilm(Film film);

    Film updateFilm(Film film);

    String deleteFilm(Integer id);

    Map<Integer, Film> getFilms();

    Film getFilm(int filmId);

    int addLikeToFilm(int filmId, int userId);

    void removeLikeFromFilm(Integer id, Integer userId);

    List<Film> mostLikedFilms(Integer count);
}
