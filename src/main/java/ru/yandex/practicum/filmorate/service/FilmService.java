package ru.yandex.practicum.filmorate.service;

import ru.yandex.practicum.filmorate.model.Film;

import java.util.Collection;
import java.util.List;

public interface FilmService {
    Film createFilm(Film film);

    Film updateFilm(Film film);

    String deleteFilm(Integer id);

    Collection<Film> getFilms();

    Film getFilm(Integer id);

    int addLikeToFilm(Integer id, Integer userId);

    void removeLikeFromFilm(Integer id, Integer userId);

    List<Film> mostLikedFilms(Integer count);
}