package ru.yandex.practicum.filmorate.dao.impl;

import lombok.extern.slf4j.Slf4j;
import ru.yandex.practicum.filmorate.dao.EntityManagerPool;
import ru.yandex.practicum.filmorate.dao.UserRepository;
import ru.yandex.practicum.filmorate.model.User;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Slf4j
@Repository
public class HibernateUserRepository implements UserRepository {


    @Override
    public List<User> getAll() {
        log.info("Получение списка всех пользователей");
        var entityManager = EntityManagerPool.getEntityManager();
        List<User> users = entityManager.createQuery("FROM User", User.class).getResultList();
        entityManager.close();
        return users;
    }

    @Override
    public Optional<User> get(long id) {
        log.info("Получение пользователя с id: {}", id);
        var entityManager = EntityManagerPool.getEntityManager();
        User user = entityManager.find(User.class, id);
        entityManager.close();
        return Optional.ofNullable(user);
    }

    @Override
    public Optional<User> save(User user) {
        log.info("Сохранение пользователя: {}", user);
        var entityManager = EntityManagerPool.getEntityManager();
        entityManager.getTransaction().begin();
        entityManager.persist(user);
        entityManager.getTransaction().commit();
        entityManager.close();
        if (user.getId() == 0)
            return Optional.empty();
        log.info("Пользователь {} сохранен", user.getId());
        return Optional.of(user);
    }

    @Override
    public Optional<User> update(User user) {
        log.info("Обновление пользователя: {}", user);
        var entityManager = EntityManagerPool.getEntityManager();
        Optional<User> optionalFilm = Optional.empty();
        entityManager.getTransaction().begin();
        User modifiedUser = entityManager.find(User.class, user.getId());
        if (modifiedUser != null) {
            optionalFilm = Optional.of(modifiedUser);
            modifiedUser.setName(user.getName());
            modifiedUser.setLogin(user.getLogin());
            modifiedUser.setEmail(user.getEmail());
            modifiedUser.setBirthday(user.getBirthday());
            entityManager.getTransaction().commit();
            entityManager.close();
            log.info("Пользователь {} обновлен", user);
        }
        return optionalFilm;
    }

    public User insertFriend(long userId, long friendId) {
        log.info("Добавление пользователю {} друга {}", userId, friendId);
        var entityManager = EntityManagerPool.getEntityManager();
        entityManager.getTransaction().begin();

        User user = entityManager.find(User.class, userId);
        User friend = entityManager.find(User.class, friendId);
        user.addFriend(friend);
        friend.addFriend(user);

        entityManager.getTransaction().commit();
        entityManager.close();
        return user;
    }

    public User deleteFriend(long userId, long friendId) {
        log.info("Удаление у пользователя {} друга {}", userId, friendId);
        var entityManager = EntityManagerPool.getEntityManager();
        entityManager.getTransaction().begin();

        User user = entityManager.find(User.class, userId);
        User friend = entityManager.find(User.class, friendId);
        user.removeFriend(friend);
        friend.removeFriend(user);

        entityManager.getTransaction().commit();
        entityManager.close();
        return user;
    }

    @Override
    public List<User> getMutualFriendsList(long id, long otherId) {
        log.info("Получение списка пересекающихся друзей у {},{}", id, otherId);
        var entityManager = EntityManagerPool.getEntityManager();
        entityManager.getTransaction().begin();
        @SuppressWarnings("unchecked") List<User> users = entityManager
                .createNativeQuery("SELECT u.* FROM friends fi " +
                        "INNER JOIN friends fo " +
                        "    ON fi.friend_id = fo.friend_id AND fi.user_id = :id AND fo.user_id = :otherId " +
                        "INNER JOIN users u " +
                        "    ON u.id = fo.friend_id", User.class)
                .setParameter("id", id)
                .setParameter("otherId", otherId)
                .getResultList();
        entityManager.getTransaction().commit();
        entityManager.close();
        return users;
    }

    @Override
    public List<User> getFriendsList(long id) {
        var entityManager = EntityManagerPool.getEntityManager();
        entityManager.getTransaction().begin();
        List<User> users = entityManager
                .createQuery("SELECT u FROM User u LEFT JOIN FETCH u.friendsList l WHERE l.id = :id",
                        User.class)
                .setParameter("id", id).getResultList();
        entityManager.getTransaction().commit();
        entityManager.close();
        return users;
    }

    @Override
    public void delete(User user) {
        log.info("Удаление пользователя: {}", user);
        var entityManager = EntityManagerPool.getEntityManager();
        entityManager.getTransaction().begin();
        entityManager.remove(user);
        entityManager.getTransaction().commit();
        entityManager.close();
        log.info("Пользователь {} удален", user);
    }
}