package ru.yandex.practicum.filmorate.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.service.FilmService;

import javax.validation.Valid;
import java.util.Collection;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/films")
public class FilmController {

    private final FilmService filmDbService;

    @PostMapping()
    public ResponseEntity<Film> postFilm(@RequestBody @Valid Film film) {

        return ResponseEntity.ok(filmDbService.createFilm(film));
    }

    @PutMapping()
    public ResponseEntity<Film> putFilm(@RequestBody @Valid Film film) {

        return ResponseEntity.ok(filmDbService.updateFilm(film));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteFilm(@PathVariable Integer id) {

        return ResponseEntity.ok(filmDbService.deleteFilm(id));
    }

    @GetMapping()
    @ResponseStatus(HttpStatus.OK)
    public Collection<Film> getFilms() {

        return filmDbService.getFilms();
    }

    @GetMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public Film getFilm(@PathVariable(required = false) Integer id) {

        return filmDbService.getFilm(id);
    }

    @PutMapping("/{id}/like/{userId}")
    @ResponseStatus(HttpStatus.OK)
    public int addLikeToFilm(@PathVariable Integer id, @PathVariable Integer userId) {

        return filmDbService.addLikeToFilm(id, userId);
    }

    @DeleteMapping("/{id}/like/{userId}")
    @ResponseStatus(HttpStatus.OK)
    public void removeLikeFromFilm(@PathVariable Integer id, @PathVariable Integer userId) {

        filmDbService.removeLikeFromFilm(id, userId);
    }

    @GetMapping("/popular")
    @ResponseStatus(HttpStatus.OK)
    public List<Film> getMostLikedFilms(@RequestParam(defaultValue = "0") Integer count) {

        return filmDbService.mostLikedFilms(count);
    }
}