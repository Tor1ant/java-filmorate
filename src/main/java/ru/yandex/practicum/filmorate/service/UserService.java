package ru.yandex.practicum.filmorate.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.Exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.UserStorage;

import java.util.*;
import java.util.stream.Collectors;

@Service
public abstract class UserService {

    private final UserStorage userStorage;
    @Autowired
    public UserService(@Qualifier("InMemoryUserStorage") UserStorage userStorage) {
        this.userStorage = userStorage;
    }

    public User addFriend(int id, int friendId) {
        notFoundValidation(id, friendId);
        userStorage.getUsers().get(id).getFriends().put(friendId, true);
        userStorage.getUsers().get(friendId).getFriends().put(id, true);
        return userStorage.getUsers().get(friendId);
    }

    public User deleteFriend(int id, int friendId) {
        notFoundValidation(id, friendId);
        userStorage.getUsers().get(id).getFriends().remove(friendId);
        userStorage.getUsers().get(friendId).getFriends().remove(id);
        return userStorage.getUsers().get(friendId);
    }

    public List<User> getUserFriends(Integer id) {
        User user = userStorage.getUser(id);
        if (!userStorage.getUsers().containsKey(id)) {
            throw new NotFoundException("Пользователя с id " + id + " не существует.");
        }
        return user.getFriends().keySet().stream()
                .map(userStorage::getUser)
                .filter(Objects::nonNull)
                .collect(Collectors.toList());
    }

    public List<User> getCommonFriends(int id, int otherId) {
        notFoundValidation(id, otherId);
        Map<Integer, Boolean> userFriends = userStorage.getUsers().get(id).getFriends();
        Map<Integer, Boolean> otherUserFriends = userStorage.getUsers().get(otherId).getFriends();
        return userFriends.keySet().stream()
                .filter(otherUserFriends::containsKey)
                .map(userStorage.getUsers()::get)
                .collect(Collectors.toList());
    }

    private void notFoundValidation(int id, int friendId) {
        if (!userStorage.getUsers().containsKey(id)) {
            throw new NotFoundException("{\"NotFoundException\":\"Пользователя с id" + id + " не существует.\"}");
        }
        if (!userStorage.getUsers().containsKey(friendId)) {
            throw new NotFoundException("{\"NotFoundException\":\"Пользователя с id" + friendId + " не существует.\"}");
        }
    }
}
