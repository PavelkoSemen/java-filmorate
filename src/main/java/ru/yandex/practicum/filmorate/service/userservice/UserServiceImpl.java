package ru.yandex.practicum.filmorate.service.userservice;

import lombok.extern.slf4j.Slf4j;
import ru.yandex.practicum.filmorate.dao.UserRepository;
import ru.yandex.practicum.filmorate.error.DAOException;
import ru.yandex.practicum.filmorate.error.RepositoryException;
import ru.yandex.practicum.filmorate.error.SaveUserException;
import ru.yandex.practicum.filmorate.error.UnknownUserException;
import ru.yandex.practicum.filmorate.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;

    @Autowired
    public UserServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public User createUser(User user) {
        log.info("Сохранение пользователя: {}", user);
        try {
            if (user.getName() == null || user.getName().isBlank()) {
                user.setName(user.getLogin());
            }
            return userRepository.save(user).orElseThrow(SaveUserException::new);
        } catch (DAOException e) {
            log.error("Ошибка сохранения пользователя: {}", user);
            throw new RepositoryException(e);
        }
    }

    @Override
    public User getUser(long id) {
        log.info("Получение пользователя по id: {}", id);
        try {
            return userRepository.get(id).orElseThrow(() -> new UnknownUserException("Пользователь не найден: " + id));
        } catch (DAOException e) {
            log.error("Ошибка получения пользователя: {}", id);
            throw new RepositoryException(e);
        }
    }

    @Override
    public User updateUser(User user) {
        log.info("Обновление пользователя {}", user);
        try {
            return userRepository.update(user).orElseThrow(() -> new UnknownUserException("Пользователь не найден: " + user));
        } catch (DAOException e) {
            log.error("Ошибка обновления пользователя: {}", user);
            throw new RepositoryException(e);
        }
    }

    @Override
    public User addFriend(long userId, long friendId) {
        userRepository.get(userId).orElseThrow(() ->
                new UnknownUserException("Пользователь не найден: " + userId));
        userRepository.get(friendId).orElseThrow(() ->
                new UnknownUserException("Пользователь не найден: " + friendId));

        return userRepository.insertFriend(userId, friendId);
    }

    @Override
    public User removeFriend(long userId, long friendId) {
        userRepository.get(userId).orElseThrow(() ->
                new UnknownUserException("Пользователь не найден: " + userId));
        userRepository.get(friendId).orElseThrow(() ->
                new UnknownUserException("Пользователь не найден: " + friendId));

        return userRepository.deleteFriend(userId, friendId);
    }

    @Override
    public List<User> getFriends(long id) {
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
        try {
            return userRepository.getAll();
        } catch (DAOException e) {
            log.error("Ошибка получения списка пользователей");
            throw new RepositoryException(e);
        }
    }
}