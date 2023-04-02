package ru.yandex.practicum.filmorate.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.Exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.service.FilmDbService;
import ru.yandex.practicum.filmorate.storage.FilmStorage;

import javax.validation.Valid;
import java.util.Collection;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/films")
public class FilmController {
    private final FilmDbService filmService;

    private final FilmStorage filmStorage;

    @PostMapping()
    public ResponseEntity<Film> postFilm(@RequestBody @Valid Film film) {
        return filmStorage.createFilm(film);

    }

    @PutMapping()
    public ResponseEntity<Film> putFilm(@RequestBody @Valid Film film) {
        return filmStorage.updateFilm(film);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteFilm(@PathVariable Integer id) {
        return filmStorage.deleteFilm(id);
    }

    @GetMapping()
    @ResponseStatus(HttpStatus.OK)
    public Collection<Film> getFilms() {
        return filmStorage.getFilms().values();
    }

    @GetMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public Film getFilm(@PathVariable(required = false) Integer id) {
        if (id != null) {
            if (id < 1 || filmStorage.getFilms().size() < id) {
                throw new NotFoundException("фильм с " + id + " не найден");
            }
            return filmStorage.getFilm(id);

        }
        throw new RuntimeException("id фильма задан не верно.");
    }

    @PutMapping("/{id}/like/{userId}")
    @ResponseStatus(HttpStatus.OK)
    public int addLikeToFilm(@PathVariable Integer id, @PathVariable Integer userId) {
      return   filmService.addLikeToFilm(id, userId);
    }

    @DeleteMapping("/{id}/like/{userId}")
    @ResponseStatus(HttpStatus.OK)
    public void removeLikeFromFilm(@PathVariable Integer id, @PathVariable Integer userId) {
        filmService.removeLikeFromFilm(id, userId);
    }

    @GetMapping("/popular")
    @ResponseStatus(HttpStatus.OK)
    public List<Film> getMostLikedFilms(@RequestParam(defaultValue = "0") Integer count) {
        return filmService.mostLikedFilms(count);
    }
}