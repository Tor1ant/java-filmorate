package ru.yandex.practicum.filmorate.storage;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.model.User;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component("UserDbStorage")
@Slf4j
@Repository
public class UserDbStorage implements UserStorage {
    private int userId = 0;
    private final JdbcTemplate jdbcTemplate;

    @Autowired
    public UserDbStorage(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public ResponseEntity<User> createUser(User user) {
        user.setId(++userId);
        String sqlQuery = "insert INTO USERS(USER_ID, EMAIL, LOGIN, NAME, BIRTHDAY) " +
                "VALUES (?, ?, ?, ?, ?)";
        jdbcTemplate.update(sqlQuery,
                user.getId(),
                user.getEmail(),
                user.getLogin(),
                user.getName(),
                user.getBirthday());
        return ResponseEntity.ok(user);
    }

    @Override
    public ResponseEntity<User> updateUser(User user) {
        String sqlQuery = "UPDATE USERS SET EMAIL =?, LOGIN =?, NAME =?, BIRTHDAY =? " +
                "WHERE USER_ID=?";
        jdbcTemplate.update(sqlQuery,
                user.getEmail(),
                user.getLogin(),
                user.getName(),
                user.getBirthday(),
                user.getId());
        return ResponseEntity.ok(user);
    }

    @Override
    public ResponseEntity<User> deleteUser(User user) {
        return null;
    }

    @Override
    public Map<Integer, User> getUsers() {
        Map<Integer, User> resultUsers = new HashMap<>();
        String sqlQuery = "SELECT * FROM USERS";
        List<User> users = jdbcTemplate.query(sqlQuery, this::mapRowToUser);
        users.forEach(user -> resultUsers.put(user.getId(), user));
        return resultUsers;
    }

    public User getUser(int userId) {
        String sqlQuery = "SELECT * FROM USERS WHERE USER_ID = ?";
        return jdbcTemplate.queryForObject(sqlQuery, this::mapRowToUser, userId);
    }

    private User mapRowToUser(ResultSet resultSet, int rowNum) throws SQLException {
        return User.builder()
                .id(resultSet.getInt("USER_ID"))
                .email(resultSet.getString("EMAIL"))
                .login(resultSet.getString("LOGIN"))
                .birthday(resultSet.getDate("BIRTHDAY").toLocalDate())
                .build();
    }
}
