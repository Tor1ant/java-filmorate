package ru.yandex.practicum.filmorate.storage;

import lombok.extern.slf4j.Slf4j;
import net.minidev.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.http.ResponseEntity;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.Exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.User;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@Repository("UserDbStorage")
public class UserDbStorage implements UserStorage {

    private final JdbcTemplate jdbcTemplate;

    @Autowired
    public UserDbStorage(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public ResponseEntity<User> createUser(User user) {
        if (user.getName() == null || user.getName().isBlank()) {
            user.setName(user.getLogin());
        }
        String sqlQuery = "insert INTO USERS( EMAIL, LOGIN, NAME, BIRTHDAY) " +
                "VALUES ( ?, ?, ?, ?)";
        jdbcTemplate.update(sqlQuery,
                user.getEmail(),
                user.getLogin(),
                user.getName(),
                user.getBirthday());

        String sqlQueryGetUser = "SELECT * FROM USERS WHERE LOGIN = ?";
        User userToResponse = jdbcTemplate.queryForObject(sqlQueryGetUser, this::mapRowToUser, user.getLogin());
        return ResponseEntity.ok(userToResponse);
    }

    @Override
    public ResponseEntity<User> updateUser(User user) {
        if (user.getName() == null || user.getName().isBlank()) {
            user.setName(user.getLogin());
        }
        String sqlQuery = "UPDATE USERS SET EMAIL =?, LOGIN =?, NAME =?, BIRTHDAY =? " +
                "WHERE USER_ID=?";
        jdbcTemplate.update(sqlQuery,
                user.getEmail(),
                user.getLogin(),
                user.getName(),
                user.getBirthday(),
                user.getId());

        String sqlQueryGetUser = "SELECT * FROM USERS WHERE USER_ID = ?";
        User userToResponse;
        try {
            userToResponse = jdbcTemplate.queryForObject(sqlQueryGetUser, this::mapRowToUser, user.getId());
        } catch (DataAccessException e) {
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("NotFoundException", "Пользователь не найден в базе данных");
            throw new NotFoundException(jsonObject.toJSONString());
        }
        return ResponseEntity.ok(userToResponse);
    }

    @Override
    public ResponseEntity<User> deleteUser(User user) {
        String sqlQuery = "DELETE  FROM FRIENDS WHERE USER_ID =1 OR FRIEND_ID =?;" +
                "DELETE  FROM LIKES WHERE USER_ID =?;" +
                "DELETE  FROM USERS WHERE USER_ID =?;";

        jdbcTemplate.update(sqlQuery, user.getId(), user.getId(), user.getId());
        return ResponseEntity.ok(user);
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
                .name(resultSet.getString("NAME"))
                .birthday(resultSet.getDate("BIRTHDAY").toLocalDate())
                .build();
    }

    public User addFriend(int id, int friendId) {
        try {
            String sqlQuery = "insert into FRIENDS(FRIEND_ID, USER_ID)" +
                    "VALUES(?, ?)";
            jdbcTemplate.update(sqlQuery, friendId, id);
        } catch (DataAccessException e) {
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("NotFoundException", "Пользователь для добавления в друзья не найден.");
            throw new NotFoundException(jsonObject.toJSONString());
        }
        return getUser(friendId);
    }

    @Override
    public User deleteFriend(int id, int friendId) {
        String sqlQuery = "DELETE  FROM FRIENDS WHERE (USER_ID = ? AND FRIEND_ID =?) OR (USER_ID =? AND FRIEND_ID = ?)";
        jdbcTemplate.update(sqlQuery, id, friendId, friendId, id);
        return getUser(friendId);
    }

    @Override
    public List<User> getUserFriends(Integer id) {
        String sqlQuery = "SELECT * from USERS where USER_ID in (select FRIEND_ID from FRIENDS where USER_ID = ?)";
        List<User> users = jdbcTemplate.query(sqlQuery, this::mapRowToUser, id);
        log.info("пользователь с id " + id + "получил список друзей состоящий из " + users.size() + " позиций");
        return users;
    }

    @Override
    public List<User> getCommonFriends(int id, int otherId) {
        String sqlQuery = "SELECT u.user_id, u.email, u.login, u.name, u.birthday " +
                "FROM FRIENDS as f1 " +
                "JOIN FRIENDS as f2 ON f2.FRIEND_ID = f1.FRIEND_ID AND f2.USER_ID = ? " +
                "JOIN USERS U on U.USER_ID = f1.FRIEND_ID " +
                "WHERE f1.USER_ID = ?";
        List<User> users = jdbcTemplate.query(sqlQuery, this::mapRowToUser, id, otherId);
        log.info("пользователь с id " + id + "получил список общих друзей с пользователем id " + otherId + " состоящий" +
                " из " + users.size() + " позиций");
        return users;
    }
}
