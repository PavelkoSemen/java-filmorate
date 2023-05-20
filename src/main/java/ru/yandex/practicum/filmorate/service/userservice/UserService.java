package ru.yandex.practicum.filmorate.service.userservice;


import ru.yandex.practicum.filmorate.model.User;

import java.util.List;

public interface UserService {
    User createUser(User user);

    User getUser(long id);

    User updateUser(User user);

    void deleteUser(long id);

    void addFriend(long userId, long friendId);
    void removeFriend(long userId, long friendId);

    List<User> getFriends(long id);

    List<User> getAllUsers();
}