package ru.yandex.practicum.filmorate.storage;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.http.ResponseEntity;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.Exception.AddLikeException;
import ru.yandex.practicum.filmorate.model.Film;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Repository
@Slf4j
public class FilmDbStorage implements FilmStorage {
    private int filmId = 0;
    private final JdbcTemplate jdbcTemplate;

    @Autowired
    public FilmDbStorage(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public ResponseEntity<Film> createFilm(Film film) {
        film.setId(++filmId);
        String sqlQuery = "INSERT INTO FILMS(film_id, name, description, release_date, duration, RATING_ID)" +
                "VALUES(?,?,?,?,?,?)";
        jdbcTemplate.update(sqlQuery,
                film.getId(),
                film.getName(),
                film.getDescription(),
                film.getReleaseDate(),
                film.getDuration(),
                film.getMpaRating());
        log.info("В коллекцию добавлен фильм " + film.getName() + "/ количество фильмов в коллекции: " + filmId);
        return ResponseEntity.ok(film);
    }

    @Override
    public ResponseEntity<Film> updateFilm(Film film) {
        String sqlQuery = "UPDATE FILMS SET NAME =?, DESCRIPTION =?, RELEASE_DATE =?, DURATION =?, RATING_ID =? " +
                "WHERE FILM_ID=?";
        jdbcTemplate.update(sqlQuery,
                film.getName(),
                film.getDescription(),
                film.getReleaseDate(),
                film.getMpaRating(),
                film.getId());
        log.info("Фильм с id " + film.getId() + " изменен на фильм " + film.getName());
        return ResponseEntity.ok(film);
    }

    @Override
    public ResponseEntity<Film> deleteFilm(Film film) {
        String sqlQuery = "DELETE FROM FILMS WHERE FILM_ID =?";
        jdbcTemplate.update(sqlQuery, film.getId());
        log.info("фильм " + film.getName() + " удален");
        filmId--;
        return ResponseEntity.ok(film);
    }

    private Film mapRowToFilm(ResultSet resultSet, int rowNum) throws SQLException {
        return Film.builder()
                .id(resultSet.getInt("FILM_ID"))
                .name(resultSet.getString("NAME"))
                .description(resultSet.getString("DESCRIPTION"))
                .releaseDate(resultSet.getDate("RELEASE_DATE").toLocalDate())
                .build();
    }

    @Override
    public Map<Integer, Film> getFilms() {
        Map<Integer, Film> resultFilms = new HashMap<>();
        String sqlQuery = "SELECT * FROM films";
        List<Film> films = jdbcTemplate.query(sqlQuery, this::mapRowToFilm);
        films.forEach(film -> resultFilms.put(film.getId(), film));
        log.info("получен список всех " + filmId + " фильмов");
        return resultFilms;
    }

    @Override
    public Film getFilm(int id) {
        String sqlQuery = "SELECT * FROM FILMS WHERE FILM_ID = ?";
        log.info("получен фильм с id " + id);
        return jdbcTemplate.queryForObject(sqlQuery, this::mapRowToFilm, id);
    }

    @Override
    public void addLikeToFilm(int filmId, int userId) {
        String sqlQuery = "INSERT INTO LIKES(FILM_ID, USER_ID) VALUES ( ?,? )";
        try {
            jdbcTemplate.update(sqlQuery, filmId, userId);
        } catch (DataAccessException e) {
            throw new AddLikeException("Невозможно поставить лайк к фильму.");
        }
    }

    @Override
    public void removeLikeFromFilm(Integer id, Integer userId) {
        String sqlQuery = "DELETE  FROM LIKES WHERE FILM_ID=? AND USER_ID =?";
        jdbcTemplate.update(sqlQuery, id, userId);
    }

    @Override
    public List<Film> mostLikedFilms(Integer count) {
        String sqlQuery = "SELECT F.NAME, COUNT(L.USER_ID)" +
                " FROM FILMS AS F " +
                "INNER JOIN LIKES L on F.FILM_ID = L.FILM_ID" +
                " GROUP BY F.NAME " +
                "ORDER BY COUNT(L.USER_ID)" +
                " DESC LIMIT ?";
        return jdbcTemplate.query(sqlQuery, this::mapRowToFilm, count);
    }
}
