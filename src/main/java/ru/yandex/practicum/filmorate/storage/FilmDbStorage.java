package ru.yandex.practicum.filmorate.storage;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.http.ResponseEntity;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.Exception.AddLikeException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.MPA;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Repository
@Slf4j
public class FilmDbStorage implements FilmStorage {

    private final JdbcTemplate jdbcTemplate;

    @Autowired
    public FilmDbStorage(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public ResponseEntity<Film> createFilm(Film film) {
        String sqlQuery = "INSERT INTO FILMS(name, description, release_date, duration, RATING_ID)" +
                "VALUES(?,?,?,?,?)";
        jdbcTemplate.update(sqlQuery,
                film.getName(),
                film.getDescription(),
                film.getReleaseDate(),
                film.getDuration(),
                film.getMpa().getId());
        log.info("В коллекцию добавлен фильм " + film.getName());
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
                film.getDuration(),
                film.getMpa().getId(),
                film.getId());
        log.info("Фильм с id " + film.getId() + " изменен на фильм " + film.getName());
        return ResponseEntity.ok(film);
    }

    @Override
    public ResponseEntity<Film> deleteFilm(Film film) {
        String sqlQuery = "DELETE FROM FILMS WHERE FILM_ID =?";
        jdbcTemplate.update(sqlQuery, film.getId());
        log.info("фильм " + film.getName() + " удален");
        return ResponseEntity.ok(film);
    }

    private Film mapRowToFilm(ResultSet resultSet, int rowNum) throws SQLException {
        return Film.builder()
                .id(resultSet.getInt("FILM_ID"))
                .name(resultSet.getString("NAME"))
                .description(resultSet.getString("DESCRIPTION"))
                .releaseDate(resultSet.getDate("RELEASE_DATE").toLocalDate())
                .duration(resultSet.getLong("DURATION"))
                .mpa(MPA.builder()
                        .id(resultSet.getInt("RATING_ID"))
                        .name(resultSet.getString("NAME"))
                        .build())
                .build();
    }

    private MPA mapRowToMPA(ResultSet resultSet, int rowNum) throws SQLException {
        return MPA.builder()
                .id(resultSet.getInt("RATING_ID"))
                .name(resultSet.getString("NAME"))
                .build();
    }

    @Override
    public Map<Integer, Film> getFilms() {
        Map<Integer, Film> resultFilms = new HashMap<>();
        String sqlQuery = "SELECT * FROM FILMS JOIN RATINGS R on FILMS.RATING_ID = R.RATING_ID";
        List<Film> films = jdbcTemplate.query(sqlQuery, this::mapRowToFilm);
        films.forEach(film -> resultFilms.put(film.getId(), film));
        log.info("получен список всех " + films.size() + " фильмов");
        return resultFilms;
    }

    @Override
    public Film getFilm(int id) {
        String sqlQuery = "SELECT * FROM FILMS JOIN RATINGS R on FILMS.RATING_ID = R.RATING_ID WHERE FILM_ID = ?";
        log.info("получен фильм с id " + id);
        Film film = jdbcTemplate.queryForObject(sqlQuery, this::mapRowToFilm, id);
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
        jdbcTemplate.update(sqlQuery, id, userId);
        log.info("пользователь " + userId + " удалил лайк с фильма с id " + id);
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
