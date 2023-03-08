package ru.yandex.practicum.filmorate.storage;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.User;

import java.util.Map;

@Component
public interface UserStorage {
    ResponseEntity<?> createUser(User user);

    ResponseEntity<?> updateUser(User user);

    ResponseEntity<?> deleteUser(User user);

    Map<Integer, User> getUsers();
}
