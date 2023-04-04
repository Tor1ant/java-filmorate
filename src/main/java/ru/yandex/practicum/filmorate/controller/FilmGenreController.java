package ru.yandex.practicum.filmorate.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.model.FilmGenre;
import ru.yandex.practicum.filmorate.service.FilmGenreDbService;

import java.util.List;

@RestController
@RequestMapping(value = "/genres")
@RequiredArgsConstructor
public class FilmGenreController {

    private final FilmGenreDbService filmGenreDbService;

    @GetMapping()
    @ResponseStatus(HttpStatus.OK)
    public List<FilmGenre> getGenres() {

        return filmGenreDbService.getGenres();
    }

    @GetMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public FilmGenre getGenreById(@PathVariable int id) {

        return filmGenreDbService.getGenreById(id);
    }
}
