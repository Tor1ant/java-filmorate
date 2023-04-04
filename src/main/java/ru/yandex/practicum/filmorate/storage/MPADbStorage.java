package ru.yandex.practicum.filmorate.storage;

import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.model.MPA;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

@Repository
@RequiredArgsConstructor
public class MPADbStorage {

    private final JdbcTemplate jdbcTemplate;

    public List<MPA> getAllMPA() {
        String sqlQuery = "SELECT* FROM RATINGS";
        return jdbcTemplate.query(sqlQuery, this::mapRowToMPA);
    }

    private MPA mapRowToMPA(ResultSet resultSet, int rowNum) throws SQLException {
        MPA mpa = new MPA(resultSet.getInt("RATING_ID"));
        mpa.setName(resultSet.getString("NAME"));
        return mpa;
    }

    public MPA getMPAById(int id) {
        String sqlQuery = "SELECT * FROM RATINGS WHERE RATING_ID = ?";
        return jdbcTemplate.queryForObject(sqlQuery, this::mapRowToMPA, id);
    }
}
