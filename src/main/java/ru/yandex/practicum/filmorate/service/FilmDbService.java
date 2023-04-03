package ru.yandex.practicum.filmorate.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.storage.FilmStorage;

import java.util.Collection;
import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
public class FilmDbService implements FilmService {
    private final FilmStorage filmDbStorage;

    public Film createFilm(Film film) {

        return filmDbStorage.createFilm(film);
    }

    public Film updateFilm(Film film) {
        validateFilmIndDb(film.getId());
        return filmDbStorage.updateFilm(film);
    }

    public String deleteFilm(Integer id) {
        validateFilmIndDb(id);
        return filmDbStorage.deleteFilm(id);
    }

    public Collection<Film> getFilms() {

        return filmDbStorage.getFilms().values();
    }

    public Film getFilm(Integer id) {
        validateFilmIndDb(id);
        return filmDbStorage.getFilm(id);
    }

    public int addLikeToFilm(Integer id, Integer userId) {
        validateFilmIndDb(id);
        return filmDbStorage.addLikeToFilm(id, userId);
    }

    public void removeLikeFromFilm(Integer id, Integer userId) {
        validateFilmIndDb(id);
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

    private void validateFilmIndDb(int filmId) {
        if (filmId < 1 || filmDbStorage.getFilms().size() < filmId) {
            throw new NotFoundException(String.format("{\"NotFoundException\":\" фильм с ID %d не найден.\"}", filmId));
        }
    }
}
