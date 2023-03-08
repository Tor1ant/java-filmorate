package ru.yandex.practicum.filmorate.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.Exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.service.FilmService;
import ru.yandex.practicum.filmorate.storage.FilmStorage;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

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
    public ResponseEntity<?> postFilm(@RequestBody @Valid Film film) {
        return filmStorage.createFilm(film);

    }

    @PutMapping()
    public ResponseEntity<?> putFilm(@RequestBody @Valid Film film) {
        return filmStorage.updateFilm(film);
    }

    @DeleteMapping
    public ResponseEntity<?> deleteFilm(@RequestBody Film film) {
        return filmStorage.deleteFilm(film);
    }

    @GetMapping("/{id}")
    public Collection<Film> getFilms(@PathVariable(required = false) Integer id) {
        if (id != null) {
            if (id < 1) {
                throw new NotFoundException("фильм с " + id + " не найден");
            }
            List<Film> film = new ArrayList<>();
            film.add(filmStorage.getFilms().get(id));
            return film;
        }
        return filmStorage.getFilms().values();
    }

}