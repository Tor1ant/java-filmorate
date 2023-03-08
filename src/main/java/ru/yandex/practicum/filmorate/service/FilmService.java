package ru.yandex.practicum.filmorate.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.storage.FilmStorage;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

@Service
public class FilmService {

    private final FilmStorage filmStorage;

    @Autowired
    public FilmService(FilmStorage filmStorage) {
        this.filmStorage = filmStorage;
    }

    public void addLikeToFilm(int id, int userId) {
        filmStorage.getFilms().get(id).getLikesByUserId().add(userId);
    }

    public void removeLikeFromFilm(int id, int userId) {
        filmStorage.getFilms().get(id).getLikesByUserId().remove(userId);
    }

    public List<Film> mostLikedFilms(int count) {
        List<Film> result = new ArrayList<>(filmStorage.getFilms().values());
        result.sort(Comparator.comparingInt(film -> film.getLikesByUserId().size()));
        if (count == 0) {
            if (filmStorage.getFilms().values().size() <= 10) {
                return result;
            } else
                result = result.subList(0, 10);

        } else
            result = result.subList(0, count + 1);
        return result;
    }
}
