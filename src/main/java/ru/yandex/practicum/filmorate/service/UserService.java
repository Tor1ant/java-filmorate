package ru.yandex.practicum.filmorate.service;

import org.springframework.web.bind.annotation.PathVariable;
import ru.yandex.practicum.filmorate.model.User;

import java.util.Collection;
import java.util.List;

public interface UserService {

    User createUser(User user);

    User updateUser(User user);

    Collection<User> getUsers();

    User deleteUser(User user);

    User getUser(@PathVariable() Integer id);

    User addFriend(int id, int friendId);

    User deleteFriend(int id, int friendId);

    List<User> getUserFriends(Integer id);

    List<User> getCommonFriends(int id, int otherId);
}
