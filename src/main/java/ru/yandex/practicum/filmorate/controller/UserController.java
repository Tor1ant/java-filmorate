package ru.yandex.practicum.filmorate.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.Exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.service.UserService;
import ru.yandex.practicum.filmorate.storage.InMemoryUserStorage;

import javax.validation.Valid;
import java.util.Collection;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/users")
public class UserController {
    private final InMemoryUserStorage userStorage;
    private final UserService userService;

    @PostMapping
    public ResponseEntity<?> postUser(@RequestBody @Valid User user) {
        return userStorage.createUser(user);
    }

    @PutMapping
    public ResponseEntity<?> putUser(@RequestBody @Valid User user) {
        return userStorage.updateUser(user);
    }

    @GetMapping()
    public Collection<User> getUsers() {
        return userStorage.getUsers().values();
    }
    @DeleteMapping
    public ResponseEntity<?> deleteFilm(@RequestBody User user) {
        return userStorage.deleteUser(user);
    }

    @GetMapping("/{id}")
    public User getUser(@PathVariable() Integer id) {
        if (id != null) {
            if (id < 1 || userStorage.getUserId() < id) {
                throw new NotFoundException("пользователь с " + id + " не найден");
            }
            return userStorage.getUsers().get(id);
        }
        throw new RuntimeException("id пользователя задан неверно.");
    }

    @PutMapping("/{id}/friends/{friendId}")
    @ResponseStatus(HttpStatus.OK)
    public User addFriend(@PathVariable Integer id, @PathVariable Integer friendId) {
        return userService.addFriend(id, friendId);
    }

    @DeleteMapping("/{id}/friends/{friendId}")
    @ResponseStatus(HttpStatus.OK)
    public User deleteFriend(@PathVariable Integer id, @PathVariable Integer friendId) {
        return userService.deleteFriend(id, friendId);
    }

    @GetMapping("/{id}/friends")
    @ResponseStatus(HttpStatus.OK)
    public List<User> getFriends(@PathVariable Integer id) {
        return userService.getUserFriends(id);
    }

    @GetMapping("/{id}/friends/common/{otherID}")
    public List<User> getCommonFriends(@PathVariable Integer id, @PathVariable Integer otherID) {
        return userService.getCommonFriends(id, otherID);
    }
}