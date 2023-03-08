package ru.yandex.practicum.filmorate.storage;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.User;

import java.util.Map;

@Component
public interface UserStorage<T extends User> {
    ResponseEntity<?> createUser(T t);

    ResponseEntity<?> updateUser(T t);

    ResponseEntity<?> deleteUser(T t);

    Map<Integer, T> getUsers();


}
