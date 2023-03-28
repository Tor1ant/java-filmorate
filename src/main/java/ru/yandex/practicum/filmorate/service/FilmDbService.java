package ru.yandex.practicum.filmorate.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.storage.FilmDbStorage;

import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
public class FilmDbService {
    private final FilmDbStorage filmDbStorage;


    public void addLikeToFilm(Integer id, Integer userId) {
        log.info("Пользователь с id " + userId + "поставил лайк фильму с id " + id);
        filmDbStorage.addLikeToFilm(id, userId);
    }

    public void removeLikeFromFilm(Integer id, Integer userId) {
        log.info("Пользователь с id " + userId + "удалил лайк с фильма с id " + id);
        filmDbStorage.removeLikeFromFilm(id, userId);
    }

    public List<Film> mostLikedFilms(Integer count) {
        List<Film> films;
        if (count == 0) {
            films = filmDbStorage.mostLikedFilms(10);
        } else {
            films = filmDbStorage.mostLikedFilms(count);
        }
        log.info("получен список фильмов с наибольшим количеством лайков, состоящий из " + films.size() + " фильмов");
        return films;
    }
}
