package ru.yandex.practicum.filmorate.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.service.UserService;
import ru.yandex.practicum.filmorate.storage.InMemoryUserStorage;

import javax.validation.Valid;
import java.util.Collection;

@RestController
@RequestMapping("/users")
public class UserController {
    private final InMemoryUserStorage userStorage;
    private final UserService userService;

    @Autowired
    public UserController(InMemoryUserStorage userStorage, UserService userService) {
        this.userStorage = userStorage;
        this.userService = userService;
    }

    @PostMapping
    public ResponseEntity<?> postUser(@RequestBody @Valid User user) {
        return userStorage.createUser(user);
    }

    @PutMapping
    public ResponseEntity<?> putUser(@RequestBody @Valid User user) {
        return userStorage.updateUser(user);
    }

    @GetMapping
    private Collection<User> getUsers() {
        return userStorage.getUsers().values();
    }
}