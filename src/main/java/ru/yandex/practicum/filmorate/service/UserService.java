package ru.yandex.practicum.filmorate.service;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.Exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.UserStorage;

import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class UserService {
    @Qualifier("InMemoryUserStorage")
    private final UserStorage userStorage;

    public User addFriend(int id, int friendId) {
        notFoundValidation(id, friendId);
        userStorage.getUsers().get(id).getFriends().put(friendId,true);
        userStorage.getUsers().get(friendId).getFriends().put(id,true);
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
        return;
    }

    public List<User> getCommonFriends(int id, int otherId) {
        notFoundValidation(id, otherId);
        Set<Integer> userFriends = userStorage.getUsers().get(id).getFriends();
        Set<Integer> otherUserFriends = userStorage.getUsers().get(otherId).getFriends();
        return userFriends.stream()
                .filter(otherUserFriends::contains)
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
