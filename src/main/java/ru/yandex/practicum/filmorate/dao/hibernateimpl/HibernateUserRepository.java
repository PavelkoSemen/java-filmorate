package ru.yandex.practicum.filmorate.dao.hibernateimpl;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.dao.UserRepository;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.utils.UsersSQL;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.transaction.Transactional;
import java.util.List;
import java.util.Optional;

@Slf4j
@Repository
public class HibernateUserRepository implements UserRepository {
    @PersistenceContext
    private EntityManager entityManager;

    public HibernateUserRepository(EntityManager entityManager) {
        this.entityManager = entityManager;
    }

    @Override
    public List<User> getAll() {
        log.info("Получение списка всех пользователей");
        List<User> users = entityManager.createQuery("FROM User", User.class).getResultList();
        return users;
    }

    @Override
    public Optional<User> get(long id) {
        log.info("Получение пользователя с id: {}", id);
        User user = entityManager.find(User.class, id);
        return Optional.ofNullable(user);
    }

    @Override
    @Transactional
    public Optional<User> save(User user) {
        log.info("Сохранение пользователя: {}", user);
        entityManager.persist(user);
        if (user.getId() == 0)
            return Optional.empty();
        log.info("Пользователь {} сохранен", user.getId());
        return Optional.of(user);
    }

    @Override
    @Transactional
    public Optional<User> update(User user) {
        log.info("Обновление пользователя: {}", user);
        Optional<User> optionalFilm = Optional.empty();
        User modifiedUser = entityManager.find(User.class, user.getId());
        if (modifiedUser != null) {
            optionalFilm = Optional.of(modifiedUser);
            modifiedUser.setName(user.getName());
            modifiedUser.setLogin(user.getLogin());
            modifiedUser.setEmail(user.getEmail());
            modifiedUser.setBirthday(user.getBirthday());
            log.info("Пользователь {} обновлен", user);
        }
        return optionalFilm;
    }

    @Override
    @Transactional
    public void insertFriend(long userId, long friendId) {
        log.info("Добавление пользователю {} друга {}", userId, friendId);
        User user = entityManager.find(User.class, userId);
        User friend = entityManager.find(User.class, friendId);
        user.addFriend(friend);
        log.info("Друг {} добавлен пользователю {}", userId, friendId);
    }

    @Override
    @Transactional
    public void deleteFriend(long userId, long friendId) {
        log.info("Удаление у пользователя {} друга {}", userId, friendId);
        User user = entityManager.find(User.class, userId);
        User friend = entityManager.find(User.class, friendId);
        user.removeFriend(friend);
        log.info("Друг {} удален у пользователя {}", userId, friendId);
    }

    @Override
    public List<User> getMutualFriendsList(long id, long otherId) {
        log.info("Получение списка пересекающихся друзей у {},{}", id, otherId);
        @SuppressWarnings("unchecked") List<User> users = entityManager
                .createNativeQuery(UsersSQL.GET_MUTUAL_FRIENDS, User.class)
                .setParameter(1, id)
                .setParameter(2, otherId)
                .getResultList();
        return users;
    }

    @Override
    public List<User> getFriendsList(long id) {
        log.info("Получение списка друзей пользователя {}", id);
        @SuppressWarnings("unchecked") List<User> users = entityManager
                .createNativeQuery(UsersSQL.GET_FRIENDS, User.class)
                .setParameter(1, id)
                .getResultList();
        return users;
    }

    @Override
    @Transactional
    public void delete(User user) {
        log.info("Удаление пользователя: {}", user);
        entityManager.remove(user);
        log.info("Пользователь {} удален", user);
    }
}