package ru.yandex.practicum.filmorate.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import ru.yandex.practicum.filmorate.model.User;

import javax.validation.Valid;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

@Slf4j
@RestController
@RequestMapping("/users")
public class UserController {
    private int userId = 0;
    private final Map<Integer, User> users = new HashMap<>();

    @PostMapping
    private ResponseEntity<?> postUser(@RequestBody @Valid User user) {
        user.setId(++userId);
        if (user.getName() == null) {
            user.setName(user.getLogin());
        }
        users.put(user.getId(), user);
        log.debug("пользователей в коллекции: " + users.size());
        return ResponseEntity.ok(user);
    }

    @PutMapping
    private ResponseEntity<?> putUser(@RequestBody @Valid User user) {
        if (user.getName() == null) {
            user.setName(user.getLogin());
        }
        if (users.containsKey(user.getId())) {
            users.put(user.getId(), user);
            log.debug("пользователь с id " + user.getId() + " изменён");
            return ResponseEntity.ok(user);
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(user);
        }
    }

    @GetMapping
    private Collection<User> getUsers() {
        return users.values();
    }
}