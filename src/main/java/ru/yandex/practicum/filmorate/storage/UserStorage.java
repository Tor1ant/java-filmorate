package ru.yandex.practicum.filmorate.storage;

import org.springframework.http.ResponseEntity;
import ru.yandex.practicum.filmorate.model.User;

import java.util.List;
import java.util.Map;

public interface UserStorage {
    ResponseEntity<User> createUser(User user);

    ResponseEntity<User> updateUser(User user);

    ResponseEntity<User> deleteUser(User user);

    Map<Integer, User> getUsers();

    User getUser(int id);

    User addFriend(int id, int friendId);

    User deleteFriend(int id, int friendId);

    List<User> getUserFriends(Integer id);

    List<User> getCommonFriends(int id, int otherId);
}
