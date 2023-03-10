package ru.yandex.practicum.filmorate.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.Exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.UserStorage;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserStorage userStorage;

    public User addFriend(int id, int friendId) {
        notFoundValidation(id, friendId);
        userStorage.getUsers().get(id).getFriends().add(friendId);
        userStorage.getUsers().get(friendId).getFriends().add(id);
        return userStorage.getUsers().get(friendId);
    }

    public User deleteFriend(int id, int friendId) {
        notFoundValidation(id, friendId);
        userStorage.getUsers().get(id).getFriends().remove(friendId);
        userStorage.getUsers().get(friendId).getFriends().remove(id);
        return userStorage.getUsers().get(friendId);
    }

    public List<User> getUserFriends(Integer id) {
        if (!userStorage.getUsers().containsKey(id)) {
            throw new NotFoundException("Пользователя с id " + id + " не существует.");
        }
        List<User> friends = new ArrayList<>();
        for (Integer friendId : userStorage.getUsers().get(id).getFriends()) {
            if (userStorage.getUsers().containsKey(friendId)) {
                friends.add(userStorage.getUsers().get(friendId));
            }
        }
        return friends;
    }

    public List<User> getCommonFriends(int id, int otherId) {
        notFoundValidation(id, otherId);
        Set<Integer> userFriends = userStorage.getUsers().get(id).getFriends();
        Set<Integer> otherUserFriends = userStorage.getUsers().get(otherId).getFriends();
        List<User> commonFriends = new ArrayList<>();
        for (Integer userFriend : userFriends) {
            if (otherUserFriends.contains(userFriend)) {
                commonFriends.add(userStorage.getUsers().get(userFriend));
            }
        }
        return commonFriends;
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
