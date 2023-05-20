package ru.yandex.practicum.filmorate.service.userservice;

import lombok.extern.slf4j.Slf4j;
import ru.yandex.practicum.filmorate.dao.DAO;
import ru.yandex.practicum.filmorate.dao.UserDAO;
import ru.yandex.practicum.filmorate.error.DAOException;
import ru.yandex.practicum.filmorate.error.RepositoryException;
import ru.yandex.practicum.filmorate.error.SaveUserException;
import ru.yandex.practicum.filmorate.error.UnknownUserException;
import ru.yandex.practicum.filmorate.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;

@Slf4j
@Service
public class UserServiceImpl implements UserService {
    private final UserDAO userDAO;

    @Autowired
    public UserServiceImpl(UserDAO userDAO) {
        this.userDAO = userDAO;
    }

    @Override
    public User createUser(User user) {
        log.info("Сохранение пользователя: {}", user);
        try {
            if (user.getName() == null) {
                user.setName(user.getLogin());
            }
            return userDAO.save(user).orElseThrow(SaveUserException::new);
        } catch (DAOException e) {
            log.error("Ошибка сохранения пользователя: {}", user);
            throw new RepositoryException(e);
        }
    }

    @Override
    public User getUser(long id) {
        log.info("Получение пользователя по id: {}", id);
        try {
            return userDAO.get(id).orElseThrow(() -> new UnknownUserException("Пользователь не найден: " + id));
        } catch (DAOException e) {
            log.error("Ошибка получения пользователя: {}", id);
            throw new RepositoryException(e);
        }
    }

    @Override
    public User updateUser(User user) {
        log.info("Обновление пользователя {}", user);
        try {
            return userDAO.update(user).orElseThrow(() -> new UnknownUserException("Пользователь не найден: " + user));
        } catch (DAOException e) {
            log.error("Ошибка обновления пользователя: {}", user);
            throw new RepositoryException(e);
        }
    }

    @Override
    public void deleteUser(long id) {
    }

    @Override
    public void addFriend(long userId, long friendId) {
        User user = userDAO.get(userId).orElseThrow(() ->
                new UnknownUserException("Пользователь не найден: " + userId));
        User friend = userDAO.get(friendId).orElseThrow(() ->
                new UnknownUserException("Пользователь не найден: " + friendId));

        user.addFriend(friend);
        friend.addFriend(user);
//        userDAO.insertFriend(userId, friendId);
        userDAO.update(user);
        userDAO.update(friend);
    }

    @Override
    public void removeFriend(long userId, long friendId) {
        userDAO.deleteFriend(userId, friendId);
    }

    @Override
    public List<User> getFriends(long id) {
        return userDAO.getFriendsList(id);
    }

//    @Override
//    public void addFriend(long userId, long friendId) {
//        userDAO.insertFriend(userId, friendId);
//    }

    @Override
    public List<User> getAllUsers() {
        log.info("Получение пользователей");
        try {
            return userDAO.getAll();
        } catch (DAOException e) {
            log.error("Ошибка получения списка пользователей");
            throw new RepositoryException(e);
        }
    }
}