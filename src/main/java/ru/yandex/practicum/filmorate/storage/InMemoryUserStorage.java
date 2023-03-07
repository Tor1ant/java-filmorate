package ru.yandex.practicum.filmorate.storage;

import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.User;

import java.util.HashMap;
import java.util.Map;

@Slf4j
@Component
@Data
public class InMemoryUserStorage implements UserStorage {
    private int userId = 0;
    private final Map<Integer, User> users = new HashMap<>();

    @Override
    public ResponseEntity<?> createUser(User user) {
        user.setId(++userId);
        if (user.getName() == null) {
            user.setName(user.getLogin());
        }
        users.put(user.getId(), user);
        log.debug("пользователей в коллекции: " + users.size());
        return ResponseEntity.ok(user);
    }

    @Override
    public ResponseEntity<?> updateUser(User user) {
        if (user.getName() == null) {
            user.setName(user.getLogin());
        }
        if (users.containsKey(user.getId())) {
            users.put(user.getId(), user);
            log.debug("пользователь с id " + user.getId() + " изменён");
            return ResponseEntity.ok(user);
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(user);
        }
    }

    @Override
    public ResponseEntity<?> deleteUser(User user) {
        return null;
    }
}
