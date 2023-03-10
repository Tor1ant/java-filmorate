package ru.yandex.practicum.filmorate.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.Exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.service.FilmService;
import ru.yandex.practicum.filmorate.storage.FilmStorage;

import javax.validation.Valid;
import java.util.Collection;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/films")
public class FilmController {
    private final FilmService filmService;
    private final FilmStorage filmStorage;

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

    @GetMapping()
    @ResponseStatus(HttpStatus.OK)
    public Collection<Film> getFilms() {
        return filmStorage.getFilms().values();
    }

    @GetMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public Film getFilms(@PathVariable(required = false) Integer id) {
        if (id != null) {
            if (id < 1 || filmStorage.getFilms().size() < id) {
                throw new NotFoundException("фильм с " + id + " не найден");
            }
            return filmStorage.getFilms().get(id);

        }
        throw new RuntimeException("id фильма задан не верно.");
    }

    @PutMapping("/{id}/like/{userId}")
    @ResponseStatus(HttpStatus.OK)
    public Film addLikeToFilm(@PathVariable Integer id, @PathVariable Integer userId) {
        return filmService.addLikeToFilm(id, userId);
    }

    @DeleteMapping("/{id}/like/{userId}")
    @ResponseStatus(HttpStatus.OK)
    public Film removeLikeFromFilm(@PathVariable Integer id, @PathVariable Integer userId) {
        return filmService.removeLikeFromFilm(id, userId);
    }

    @GetMapping("/popular")
    @ResponseStatus(HttpStatus.OK)
    public List<Film> getMostLikedFilms(@RequestParam(defaultValue = "0") Integer count) {
        return filmService.mostLikedFilms(count);
    }
}