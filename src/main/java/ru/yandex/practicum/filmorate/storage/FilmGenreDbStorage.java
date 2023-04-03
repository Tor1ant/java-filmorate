package ru.yandex.practicum.filmorate.storage;

import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.model.FilmGenre;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

@Repository
@RequiredArgsConstructor
public class FilmGenreDbStorage {

    private final JdbcTemplate jdbcTemplate;

    public List<FilmGenre> getFilmGenres() {
        String sqlQuery = "SELECT* FROM GENRES";
        return jdbcTemplate.query(sqlQuery, this::mapRowToFilmGenre);
    }

    private FilmGenre mapRowToFilmGenre(ResultSet resultSet, int rowNum) throws SQLException {
        return FilmGenre.builder()
                .id(resultSet.getInt("GENRE_ID"))
                .name(resultSet.getString("NAME"))
                .build();
    }

    public FilmGenre getGenreById(int id) {
        String sqlQuery = "SELECT * FROM GENRES WHERE GENRE_ID = ?";
        return jdbcTemplate.queryForObject(sqlQuery, this::mapRowToFilmGenre, id);
    }
}
