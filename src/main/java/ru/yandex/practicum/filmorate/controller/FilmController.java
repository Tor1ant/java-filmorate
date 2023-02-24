package ru.yandex.practicum.filmorate.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.Exception.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;

import javax.validation.Valid;
import java.time.LocalDate;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

@Slf4j
@RestController
@RequestMapping("/films")
public class FilmController {
    private int filmId = 0;
    private final Map<Integer, Film> films = new HashMap<>();
    private final LocalDate BIRTH_OF_FILMS = LocalDate.of(1895, 12, 28);

    @PostMapping()
    private ResponseEntity<?> postFilm(@RequestBody @Valid Film film) {
        if (film.getReleaseDate().isBefore(BIRTH_OF_FILMS)) {
            throw new ValidationException("{\"validationException\":\"Дата создания фильма не может быть раньше " +
                    "1895,12,28\"}");
        }
        film.setId(++filmId);
        films.put(film.getId(), film);
        log.debug("Фильмов в коллекции: " + films.size());
        return ResponseEntity.ok(film);
    }

    @PutMapping()
    private ResponseEntity<?> putFilm(@RequestBody @Valid Film film) {
        if (film.getReleaseDate().isBefore(BIRTH_OF_FILMS)) {
            throw new ValidationException("{\"validationException\":\"Дата создания фильма не может быть раньше " +
                    "1895,12,28\"}");
        }
        if (films.containsKey(film.getId())) {
            films.put(film.getId(), film);
            log.debug("Фильм с id " + film.getId() + " изменён");
            return ResponseEntity.ok(film);
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(film);
        }
    }

    @GetMapping()
    private Collection<Film> getFilms() {
        return films.values();
    }
}