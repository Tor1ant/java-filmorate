package ru.yandex.practicum.filmorate.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.model.FilmGenre;
import ru.yandex.practicum.filmorate.storage.FilmGenreDbStorage;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class FilmGenreDbService {
    private final FilmGenreDbStorage filmGenreDbStorage;

    public List<FilmGenre> getGenres() {
        List<FilmGenre> filmGenres = filmGenreDbStorage.getFilmGenres();
        log.info("получен список жанров фильмов");
        return filmGenres;
    }

    public FilmGenre getGenreById(int id) {
        FilmGenre filmGenre = filmGenreDbStorage.getGenreById(id);
        log.info("получен жанр фильма " + filmGenre.getName());
        return filmGenre;
    }
}
