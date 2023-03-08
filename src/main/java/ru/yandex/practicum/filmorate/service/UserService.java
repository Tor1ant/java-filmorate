package ru.yandex.practicum.filmorate.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.UserStorage;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@Service
public class UserService {
    private final UserStorage<?> userStorage;

    @Autowired
    public UserService(UserStorage<?> userStorage) {
        this.userStorage = userStorage;
    }

    public void addFriend(int id, int friendId) {
        userStorage.getUsers().get(id).getFriends().add(friendId);
        userStorage.getUsers().get(friendId).getFriends().add(id);
    }

    public void deleteFriend(int id, int friendId) {
        userStorage.getUsers().get(id).getFriends().remove(friendId);
        userStorage.getUsers().get(friendId).getFriends().remove(id);
    }

    public List<User> getCommonFriends(int id, int otherId) {
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
}
