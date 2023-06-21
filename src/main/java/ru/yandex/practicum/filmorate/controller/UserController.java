package ru.yandex.practicum.filmorate.controller;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.service.userservice.UserService;

import javax.validation.Valid;
import java.util.List;

@RestController
public class UserController {
    private final UserService userService;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping(path = "/users/{userId}")
    public User getUser(@PathVariable("userId") Long id) {
        return userService.getUser(id);
    }

    @GetMapping(path = "/users")
    public List<User> getUsers() {
        return userService.getAllUsers();
    }

    @PostMapping(path = "/users")
    public User addUser(@RequestBody @Valid User user) {
        return userService.createUser(user);
    }

    @PutMapping(path = "/users")
    public User updateUser(@RequestBody @Valid User user) {
        return userService.updateUser(user);
    }

    @PutMapping(path = "/users/{id}/friends/{friendId}")
    public void addFriends(@PathVariable long id, @PathVariable long friendId) {
        userService.addFriend(id, friendId);
    }

    @DeleteMapping(path = "/users/{id}/friends/{friendId}")
    public void deletedFriends(@PathVariable long id, @PathVariable long friendId) {
        userService.removeFriend(id, friendId);
    }

    @GetMapping("/users/{id}/friends")
    public List<User> getAllFriends(@PathVariable long id) {
        return userService.getFriends(id);
    }

    @GetMapping("/users/{id}/friends/common/{otherId}")
    public List<User> getMutualFriends(@PathVariable long id, @PathVariable long otherId) {
        return userService.getMutualFriends(id, otherId);
    }

    @DeleteMapping(path = "/users/{userId}")
    public User deleteUser(@PathVariable long userId) {
        return userService.deleteUser(userId);
    }
}