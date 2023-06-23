package ru.yandex.practicum.filmorate.dao;

import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.User;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

public interface UserRepository {
    List<User> getAll();

    Optional<User> get(long id);

    Optional<User> save(User t);

    Optional<User> update(User t);

    boolean insertFriend(long userId, long friendId);

    boolean deleteFriend(long userId, long friendId);

    List<User> getMutualFriendsList(long id, long otherId);

    List<User> getFriendsList(long id);

    void delete(User t);

    Collection<Film> getRecommendations(long id);
}