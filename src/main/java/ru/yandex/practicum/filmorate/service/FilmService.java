package ru.yandex.practicum.filmorate.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.Exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.storage.FilmStorage;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

@Service
@RequiredArgsConstructor
public class FilmService {
    private final FilmStorage filmStorage;

    public Film addLikeToFilm(int id, int userId) {
        notFoundFilmValidation(id, userId);
        filmStorage.getFilms().get(id).getLikesByUserId().add(userId);
        return filmStorage.getFilms().get(id);
    }

    public Film removeLikeFromFilm(int id, int userId) {
        notFoundFilmValidation(id, userId);
        filmStorage.getFilms().get(id).getLikesByUserId().remove(userId);
        return filmStorage.getFilms().get(id);
    }

    public List<Film> mostLikedFilms(int count) {
        List<Film> result = new ArrayList<>(filmStorage.getFilms().values());
        if (count == 0) {
            if (filmStorage.getFilms().values().size() <= 10) {
                result.sort(Comparator.comparingInt((Film film) -> film.getLikesByUserId().size()).reversed());
                return result;
            } else
                result = result.subList(0, 10);
            result.sort(Comparator.comparingInt((Film film) -> film.getLikesByUserId().size()).reversed());
            return result;

        } else
            result.sort(Comparator.comparingInt((Film film) -> film.getLikesByUserId().size()).reversed());
        result = result.subList(0, count);
        return result;
    }

    private void notFoundFilmValidation(int id, int userId) {
        if (!filmStorage.getFilms().containsKey(id)) {
            throw new NotFoundException("{\"NotFoundException\":\"Фильма с таким id" + id + " не существует.\"}");
        }
        if (!filmStorage.getFilms().containsKey(userId)) {
            throw new NotFoundException("{\"NotFoundException\":\"Пользователя с id" + userId + " не существует.\"}");
        }
    }
}
