package ru.yandex.practicum.filmorate.dao;

import ru.yandex.practicum.filmorate.model.User;

import java.util.List;
import java.util.Optional;

public interface UserRepository {
    List<User> getAll();

    Optional<User> get(long id);

    Optional<User> save(User t);

    Optional<User> update(User t);

    User insertFriend(long userId, long friendId);

    User deleteFriend(long userId, long friendId);

    List<User> getMutualFriendsList(long id, long otherId);

    List<User> getFriendsList(long id);

    void delete(User t);
}