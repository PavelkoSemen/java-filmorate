package ru.yandex.practicum.filmorate.service.userservice;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.dao.EventRepository;
import ru.yandex.practicum.filmorate.dao.UserRepository;
import ru.yandex.practicum.filmorate.error.SaveUserException;
import ru.yandex.practicum.filmorate.error.UnknownUserException;
import ru.yandex.practicum.filmorate.model.Event;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.model.eventenum.EventOperation;
import ru.yandex.practicum.filmorate.model.eventenum.EventType;
import ru.yandex.practicum.filmorate.utils.customannotations.EventFeed;

import java.util.Collection;
import java.util.List;

@Slf4j
@Service
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
    private final EventRepository eventRepository;

    @Autowired
    public UserServiceImpl(UserRepository userRepository, EventRepository eventRepository) {
        this.userRepository = userRepository;
        this.eventRepository = eventRepository;
    }

    @Override
    public User createUser(User user) {
        log.info("Сохранение пользователя: {}", user);
        if (user.getName() == null || user.getName().isBlank()) {
            user.setName(user.getLogin());
        }
        return userRepository.save(user).orElseThrow(SaveUserException::new);
    }

    @Override
    public User getUser(long id) {
        log.info("Получение пользователя по id: {}", id);
        return userRepository.get(id).orElseThrow(() -> new UnknownUserException("Пользователь не найден: " + id));
    }

    @Override
    public User updateUser(User user) {
        log.info("Обновление пользователя {}", user);
        return userRepository.update(user).orElseThrow(() -> new UnknownUserException("Пользователь не найден: " + user));
    }

    @Override
    @EventFeed(operation = EventOperation.ADD, type = EventType.FRIEND)
    public boolean addFriend(long userId, long friendId) {
        userRepository.get(userId).orElseThrow(() ->
                new UnknownUserException("Пользователь не найден: " + userId));
        userRepository.get(friendId).orElseThrow(() ->
                new UnknownUserException("Пользователь не найден: " + friendId));
        return userRepository.insertFriend(userId, friendId);
    }

    @Override
    @EventFeed(operation = EventOperation.REMOVE, type = EventType.FRIEND)
    public boolean removeFriend(long userId, long friendId) {
        userRepository.get(userId).orElseThrow(() ->
                new UnknownUserException("Пользователь не найден: " + userId));
        userRepository.get(friendId).orElseThrow(() ->
                new UnknownUserException("Пользователь не найден: " + friendId));

        return userRepository.deleteFriend(userId, friendId);
    }

    @Override
    public List<User> getFriends(long id) {
        userRepository.get(id).orElseThrow(() ->
                new UnknownUserException("Пользователь не найден: " + id));
        return userRepository.getFriendsList(id);
    }

    @Override
    public List<User> getMutualFriends(long id, long otherId) {
        userRepository.get(id).orElseThrow(() ->
                new UnknownUserException("Пользователь не найден: " + id));
        userRepository.get(otherId).orElseThrow(() ->
                new UnknownUserException("Пользователь не найден: " + otherId));

        return userRepository.getMutualFriendsList(id, otherId);
    }

    @Override
    public List<User> getAllUsers() {
        log.info("Получение пользователей");
        return userRepository.getAll();
    }

    @Override
    public User deleteUser(long id) {
        log.info("Удаление пользователя по id: {}:", id);
        var user = getUser(id);
        userRepository.delete(user);
        return user;
    }

    @Override
    public Collection<Film> getRecommendations(long id) {
        log.info("Получение рекомендаций по id: {}:", id);
        return userRepository.getRecommendations(id);
    }

    @Override
    public List<Event> getUsersEventFeed(long userId) {
        userRepository.get(userId).orElseThrow(() ->
                new UnknownUserException("Пользователь не найден: " + userId));
        return eventRepository.getEventFeed(userId);
    }
}