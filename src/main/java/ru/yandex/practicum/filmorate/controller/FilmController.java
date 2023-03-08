package ru.yandex.practicum.filmorate.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.service.FilmService;
import ru.yandex.practicum.filmorate.storage.FilmStorage;


import javax.validation.Valid;
import java.util.Collection;

@RestController
@RequestMapping("/films")
public class FilmController {
    private final FilmService filmService;
    private final FilmStorage filmStorage;

    @Autowired
    public FilmController(FilmService filmService, FilmStorage filmStorage) {
        this.filmService = filmService;
        this.filmStorage = filmStorage;
    }

    @PostMapping()
    private ResponseEntity<?> postFilm(@RequestBody @Valid Film film) {
        return filmStorage.createFilm(film);

    }

    @PutMapping()
    private ResponseEntity<?> putFilm(@RequestBody @Valid Film film) {
        return filmStorage.updateFilm(film);
    }

    @GetMapping()
    private Collection<Film> getFilms() {
        return filmStorage.getFilms().values();
    }
}