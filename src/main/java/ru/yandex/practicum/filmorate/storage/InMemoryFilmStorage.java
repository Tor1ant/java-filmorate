package ru.yandex.practicum.filmorate.storage;

import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.RequestBody;
import ru.yandex.practicum.filmorate.Exception.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;

import javax.validation.Valid;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;

@Slf4j
@Component
public class InMemoryFilmStorage implements FilmStorage {
    private int filmId = 0;
    @Getter
    private final Map<Integer, Film> films = new HashMap<>();
    private static final LocalDate BIRTH_OF_FILMS = LocalDate.of(1895, 12, 28);

    @Override
    public ResponseEntity<Film> createFilm(Film film) {
        validateFilm(film);
        film.setId(++filmId);
        films.put(film.getId(), film);
        log.debug(film.toString());
        return ResponseEntity.ok(film);
    }

    @Override
    public ResponseEntity<Film> updateFilm(@RequestBody @Valid Film film) {
        if (films.containsKey(film.getId())) {
            validateFilm(film);
            films.put(film.getId(), film);
            log.debug("Фильм с id " + film.getId() + " изменён");
            return ResponseEntity.ok(film);
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(film);
        }
    }

    @Override
    public ResponseEntity<Film> deleteFilm(Film film) {
        if (films.containsKey(film.getId())) {
            films.remove(film.getId());
            log.debug("Фильм с id " + film.getId() + " удалён");
            return ResponseEntity.ok(film);
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(film);
        }
    }

    private void validateFilm(Film film) {
        if (film.getReleaseDate().isBefore(BIRTH_OF_FILMS)) {
            throw new ValidationException("{\"validationException\":\"Дата создания фильма не может быть раньше " +
                    "1895,12,28\"}");
        }
    }
}
