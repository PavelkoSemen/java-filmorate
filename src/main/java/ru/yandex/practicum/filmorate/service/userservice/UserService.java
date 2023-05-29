package ru.yandex.practicum.filmorate.service.userservice;


import ru.yandex.practicum.filmorate.model.User;

import java.util.List;

public interface UserService {
    User createUser(User user);

    User getUser(long id);

    User updateUser(User user);

    User addFriend(long userId, long friendId);
    User removeFriend(long userId, long friendId);

    List<User> getFriends(long id);
    List<User> getMutualFriends(long id, long otherId);

    List<User> getAllUsers();
}