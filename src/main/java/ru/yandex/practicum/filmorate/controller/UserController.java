package ru.yandex.practicum.filmorate.controller;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.*;

import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.service.userservice.UserService;

import javax.validation.Valid;
import javax.validation.ValidationException;
import java.util.List;
import java.util.stream.Collectors;

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
    public User addUser(@RequestBody @Valid User user, Errors errors) {
        if (errors.hasErrors()) {
            throw new ValidationException(this.getErrorsMessage(errors));
        }
        return userService.createUser(user);
    }

    @PutMapping(path = "/users")
    public User updateUser(@RequestBody @Valid User user, Errors errors) {
        if (errors.hasErrors()) {
            throw new ValidationException(this.getErrorsMessage(errors));
        }
        return userService.updateUser(user);
    }

    private String getErrorsMessage(Errors errors) {
        return errors.getAllErrors().stream()
                .map(DefaultMessageSourceResolvable::getDefaultMessage)
                .collect(Collectors.joining("\n"));
    }
}