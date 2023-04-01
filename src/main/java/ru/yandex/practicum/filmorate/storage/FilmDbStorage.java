package ru.yandex.practicum.filmorate.storage;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.http.ResponseEntity;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.Exception.AddLikeException;
import ru.yandex.practicum.filmorate.Exception.NotFoundException;
import ru.yandex.practicum.filmorate.Exception.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.FilmGenre;
import ru.yandex.practicum.filmorate.model.MPA;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.*;

@Repository
@Slf4j
public class FilmDbStorage implements FilmStorage {

    private final JdbcTemplate jdbcTemplate;
    private static final LocalDate BIRTH_OF_FILMS = LocalDate.of(1895, 12, 28);

    @Autowired
    public FilmDbStorage(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public ResponseEntity<Film> createFilm(Film film) {
        validateFilm(film);
        log.info("В коллекцию добавлен фильм " + film.getName());
        SimpleJdbcInsert simpleJdbcInsert = new SimpleJdbcInsert(jdbcTemplate)
                .withTableName("FILMS")
                .usingGeneratedKeyColumns("FILM_ID");
        int filmId = simpleJdbcInsert.executeAndReturnKey(film.toMap()).intValue();

        Optional<Set<FilmGenre>> filmGenres = Optional.ofNullable(film.getGenres());
        if (filmGenres.isPresent()) {
            simpleJdbcInsert = new SimpleJdbcInsert(jdbcTemplate);
            for (FilmGenre genre : film.getGenres()) {
                Map<String, Integer> data = new HashMap<>();
                data.put("FILM_ID", filmId);
                data.put("GENRE_ID", genre.getId());
                simpleJdbcInsert.withTableName("FILM_GENRE").usingColumns("FILM_ID", "GENRE_ID")
                        .execute(data);
            }
        }
        return ResponseEntity.ok(getFilm(filmId));
    }

    @Override
    public ResponseEntity<Film> updateFilm(Film film) {
        validateFilm(film);
        String sqlQuery = "UPDATE FILMS SET NAME =?, DESCRIPTION =?, RELEASE_DATE =?, DURATION =?, RATING_ID =? " +
                "WHERE FILM_ID=?";
        jdbcTemplate.update(sqlQuery,
                film.getName(),
                film.getDescription(),
                film.getReleaseDate(),
                film.getDuration(),
                film.getMpa().getId(),
                film.getId());

        log.info("Фильм с id " + film.getId() + " изменен на фильм " + film.getName());
        return ResponseEntity.ok(getFilm(film.getId()));
    }

    @Override
    public ResponseEntity<Film> deleteFilm(Film film) {
        String sqlQuery = "DELETE FROM FILMS WHERE FILM_ID =?";
        jdbcTemplate.update(sqlQuery, film.getId());
        log.info("фильм " + film.getName() + " удален");
        return ResponseEntity.ok(film);
    }

    private Film mapRowToFilm(ResultSet resultSet, int rowNum) throws SQLException {
        Film film = Film.builder()
                .id(resultSet.getInt("FILM_ID"))
                .name(resultSet.getString("FILM_NAME"))
                .description(resultSet.getString("DESCRIPTION"))
                .releaseDate(resultSet.getDate("RELEASE_DATE").toLocalDate())
                .duration(resultSet.getLong("DURATION"))
                .mpa(new MPA(resultSet.getInt("RATING_ID"), resultSet.getString("RATING_NAME")))
                .build();

        List<FilmGenre> filmGenreSet = getGenresForFilm(film.getId());
        film.setGenres(new HashSet<>(filmGenreSet));
        return film;
    }

    private List<FilmGenre> getGenresForFilm(int filmId) {
        String sqlQuery = "SELECT G.GENRE_ID, G.NAME " +
                "FROM GENRES AS G " +
                "JOIN FILM_GENRE AS FG ON G.GENRE_ID = FG.GENRE_ID " +
                "WHERE FG.FILM_ID =?";
        return jdbcTemplate.query(sqlQuery, (resultSet, rowNum) ->
                        new FilmGenre(resultSet.getInt("GENRE_ID"), resultSet.getString("NAME")),
                filmId);
    }

    @Override
    public Map<Integer, Film> getFilms() {
        Map<Integer, Film> resultFilms = new HashMap<>();
        String sqlQuery = "SELECT F.FILM_ID, F.NAME AS FILM_NAME, F.DESCRIPTION, F.RELEASE_DATE, F.DURATION," +
                "F.RATING_ID, R.RATING_ID AS RATING_ID, R.NAME AS RATING_NAME " +
                "FROM FILMS AS F " +
                "JOIN RATINGS AS R ON F.RATING_ID = R.RATING_ID";
        List<Film> films = jdbcTemplate.query(sqlQuery, this::mapRowToFilm);
        films.forEach(film -> resultFilms.put(film.getId(), film));
        log.info("получен список всех " + films.size() + " фильмов");
        return resultFilms;
    }

    @Override
    public Film getFilm(int id) {
        String sqlQuery = "SELECT F.FILM_ID, F.NAME AS FILM_NAME, F.DESCRIPTION, F.RELEASE_DATE, F.DURATION," +
                "F.RATING_ID, R.RATING_ID AS RATING_ID, R.NAME AS RATING_NAME " +
                "FROM FILMS AS F " +
                "JOIN RATINGS AS R ON F.RATING_ID = R.RATING_ID " +
                "WHERE F.FILM_ID = ?";
        log.info("получен фильм с id " + id);
        Film film;
        try {
            film = jdbcTemplate.queryForObject(sqlQuery, this::mapRowToFilm, id);
        } catch (DataAccessException e) {
            throw new NotFoundException(String.format("{\"NotFoundException\":\" фильм с ID %d не найден.\"}", id));
        }
        log.info("получен фильм: " + (film != null ? film.getName() : null));
        return film;
    }

    @Override
    public void addLikeToFilm(int filmId, int userId) {
        String sqlQuery = "INSERT INTO LIKES(FILM_ID, USER_ID) VALUES ( ?,? )";
        try {
            jdbcTemplate.update(sqlQuery, filmId, userId);
        } catch (DataAccessException e) {
            throw new AddLikeException("Невозможно поставить лайк к фильму.");
        }
        log.info("пользователь " + userId + " поставил лайк фильму с id " + filmId);
    }

    @Override
    public void removeLikeFromFilm(Integer id, Integer userId) {
        String sqlQuery = "DELETE  FROM LIKES WHERE FILM_ID=? AND USER_ID =?";
        int rowsAffected = jdbcTemplate.update(sqlQuery, id, userId);
        if (rowsAffected == 0) {
            throw new NotFoundException("Невозможно удалить лайк с фильма.");
        }
        log.info("пользователь " + userId + " удалил лайк с фильма с id " + id);
    }

    @Override
    public List<Film> mostLikedFilms(Integer count) {
        String sqlQuery = "SELECT F.FILM_ID, F.NAME AS FILM_NAME, F.DESCRIPTION, F.RELEASE_DATE, F.DURATION," +
                " F.RATING_ID, R.RATING_ID AS RATING_ID, R.NAME AS RATING_NAME " +
                "FROM FILMS AS F " +
                "LEFT JOIN LIKES L on F.FILM_ID = L.FILM_ID " +
                "LEFT JOIN RATINGS AS R ON F.RATING_ID = R.RATING_ID " +
                "GROUP BY F.NAME " +
                "ORDER BY COUNT(L.USER_ID) " +
                "DESC LIMIT ?";
        return jdbcTemplate.query(sqlQuery, this::mapRowToFilm, count);
    }

    private void validateFilm(Film film) {
        if (film.getReleaseDate().isBefore(BIRTH_OF_FILMS)) {
            throw new ValidationException("{\"validationException\":\"Дата создания фильма не может быть раньше " +
                    "1895,12,28\"}");
        }
    }
}
