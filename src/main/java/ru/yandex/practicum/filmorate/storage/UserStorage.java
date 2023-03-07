package ru.yandex.practicum.filmorate.storage;

import org.springframework.http.ResponseEntity;
import ru.yandex.practicum.filmorate.model.User;

public interface UserStorage {
    ResponseEntity<?> createUser(User user);
    ResponseEntity<?> updateUser(User user);
    ResponseEntity<?> deleteUser(User user);


}
