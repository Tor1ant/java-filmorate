package ru.yandex.practicum.filmorate.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.UserStorage;

import java.util.List;

@Service
@Slf4j
public class UserDbService {

    private final UserStorage userStorage;

    public UserDbService(@Qualifier("UserDbStorage") UserStorage userStorage) {
        this.userStorage = userStorage;
    }

    public User addFriend(int id, int friendId) {
        log.info("Пользователь с id " + id + " добавил в друзья пользователя с id " + friendId);
        return userStorage.addFriend(id, friendId);
    }

    public User deleteFriend(int id, int friendId) {
        log.info("Пользователь с id " + id + " удалил из друзей пользователя с id " + friendId);
        return userStorage.deleteFriend(id, friendId);
    }

    public List<User> getUserFriends(Integer id) {
        return userStorage.getUserFriends(id);
    }

    public List<User> getCommonFriends(int id, int otherId) {
        return userStorage.getCommonFriends(id, otherId);
    }
}
