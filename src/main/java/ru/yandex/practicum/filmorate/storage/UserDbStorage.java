package ru.yandex.practicum.filmorate.storage;

import org.springframework.http.ResponseEntity;
import ru.yandex.practicum.filmorate.model.User;

import java.util.Map;

public class UserDbStorage implements UserStorage {
    @Override
    public ResponseEntity<User> createUser(User user) {
        return null;
    }

    @Override
    public ResponseEntity<User> updateUser(User user) {
        return null;
    }

    @Override
    public ResponseEntity<User> deleteUser(User user) {
        return null;
    }

    @Override
    public Map<Integer, User> getUsers() {
        return null;
    }
}
