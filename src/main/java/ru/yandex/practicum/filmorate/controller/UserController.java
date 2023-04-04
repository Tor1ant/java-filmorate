package ru.yandex.practicum.filmorate.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.service.UserService;

import javax.validation.Valid;
import java.util.Collection;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/users")
public class UserController {

    private final UserService userDbService;

    @PostMapping
    public ResponseEntity<User> postUser(@RequestBody @Valid User user) {

        return ResponseEntity.ok(userDbService.createUser(user));
    }

    @PutMapping
    public ResponseEntity<User> putUser(@RequestBody @Valid User user) {

        return ResponseEntity.ok(userDbService.updateUser(user));
    }

    @ResponseStatus(HttpStatus.OK)
    @GetMapping()
    public Collection<User> getUsers() {

        return userDbService.getUsers();
    }

    @ResponseStatus(HttpStatus.OK)
    @DeleteMapping
    public ResponseEntity<User> deleteUser(@RequestBody User user) {

        return ResponseEntity.ok(userDbService.deleteUser(user));
    }

    @GetMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public User getUser(@PathVariable() Integer id) {

        return userDbService.getUser(id);
    }

    @PutMapping("/{id}/friends/{friendId}")
    @ResponseStatus(HttpStatus.OK)
    public User addFriend(@PathVariable Integer id, @PathVariable Integer friendId) {

        return userDbService.addFriend(id, friendId);
    }

    @DeleteMapping("/{id}/friends/{friendId}")
    @ResponseStatus(HttpStatus.OK)
    public User deleteFriend(@PathVariable Integer id, @PathVariable Integer friendId) {

        return userDbService.deleteFriend(id, friendId);
    }

    @GetMapping("/{id}/friends")
    @ResponseStatus(HttpStatus.OK)
    public List<User> getFriends(@PathVariable Integer id) {

        return userDbService.getUserFriends(id);
    }

    @ResponseStatus(HttpStatus.OK)
    @GetMapping("/{id}/friends/common/{otherID}")
    public List<User> getCommonFriends(@PathVariable Integer id, @PathVariable Integer otherID) {

        return userDbService.getCommonFriends(id, otherID);
    }
}
