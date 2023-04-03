package ru.yandex.practicum.filmorate.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PathVariable;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.UserStorage;

import java.util.Collection;
import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
public class UserDbService implements UserService {

    private final UserStorage userStorage;

    public User createUser(User user) {

        return userStorage.createUser(user);
    }

    public User updateUser(User user) {

        return userStorage.updateUser(user);
    }

    public Collection<User> getUsers() {

        return userStorage.getUsers().values();
    }

    public User deleteUser(User user) {

        return userStorage.deleteUser(user);
    }

    public User getUser(@PathVariable() Integer id) {
        if (id != null) {
            if (id < 1 || userStorage.getUsers().size() < id) {
                throw new NotFoundException("пользователь с " + id + " не найден");
            }
            return userStorage.getUser(id);
        }
        throw new RuntimeException("id пользователя задан неверно.");
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
