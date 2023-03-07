package ru.yandex.practicum.filmorate.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.storage.InMemoryFilmStorage;

import javax.validation.Valid;
import java.util.Collection;

@RestController
@RequestMapping("/films")
public class FilmController {
    private final InMemoryFilmStorage inMemoryFilmStorage;

    @Autowired
    public FilmController(InMemoryFilmStorage inMemoryFilmStorage) {
        this.inMemoryFilmStorage = inMemoryFilmStorage;
    }

    @PostMapping()
    private ResponseEntity<?> postFilm(@RequestBody @Valid Film film) {
        return inMemoryFilmStorage.createFilm(film);

    }

    @PutMapping()
    private ResponseEntity<?> putFilm(@RequestBody @Valid Film film) {
        return inMemoryFilmStorage.updateFilm(film);
    }

    @GetMapping()
    private Collection<Film> getFilms() {
        return inMemoryFilmStorage.getFilms().values();
    }
}