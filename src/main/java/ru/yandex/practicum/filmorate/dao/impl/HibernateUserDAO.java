package ru.yandex.practicum.filmorate.dao.impl;

import lombok.extern.slf4j.Slf4j;
import ru.yandex.practicum.filmorate.dao.DAO;
import ru.yandex.practicum.filmorate.dao.EntityManagerPool;
import ru.yandex.practicum.filmorate.error.DAOException;
import ru.yandex.practicum.filmorate.model.User;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Slf4j
@Repository
public class HibernateUserDAO implements DAO<User> {


    @Override
    public List<User> getAll() {
        log.info("Получение списка всех пользователей");
        try (var entityManager = EntityManagerPool.getEntityManager();) {
            return entityManager.createQuery("FROM User", User.class).getResultList();
        } catch (IllegalStateException e) {
            log.error("Ошибка получения пользователей");
            throw new DAOException("Ошибка получения пользователей", e);
        }

    }

    @Override
    public Optional<User> get(long id) {
        log.info("Получение пользователя с id: {}", id);
        try (var entityManager = EntityManagerPool.getEntityManager()) {
            User user = entityManager.find(User.class, id);
            return Optional.of(user);
        } catch (IllegalStateException e) {
            log.error("Ошибка получения пользователя с id: {}", id);
            throw new DAOException("Ошибка получения пользователя с id: " + id, e);
        }
    }

    @Override
    public Optional<User> save(User user) {
        log.info("Сохранение пользователя: {}", user);
        try (var entityManager = EntityManagerPool.getEntityManager()) {
            entityManager.getTransaction().begin();
            entityManager.persist(user);
            entityManager.getTransaction().commit();
            if (user.getId() == 0)
                return Optional.empty();
            log.info("Пользователь {} сохранен", user.getId());
            return Optional.of(user);
        } catch (IllegalStateException e) {
            log.error("Ошибка сохранения пользователя: {}", user);
            throw new DAOException("Ошибка сохранения пользователя: " + user, e);
        }
    }

    @Override
    public Optional<User> update(User user) {
        log.info("Обновление пользователя: {}", user);
        try (var entityManager = EntityManagerPool.getEntityManager()) {
            Optional<User> optionalFilm = Optional.empty();
            User modifiedUser = entityManager.find(User.class, user.getId());
            if (modifiedUser != null) {
                optionalFilm = Optional.of(modifiedUser);
                entityManager.getTransaction().begin();
                modifiedUser.setName(user.getName());
                modifiedUser.setLogin(user.getLogin());
                modifiedUser.setEmail(user.getEmail());
                modifiedUser.setBirthday(user.getBirthday());
                entityManager.getTransaction().commit();
                log.info("Пользователь {} обновлен", user);
            }
            return optionalFilm;
        } catch (IllegalStateException e) {
            log.error("Ошибка обновления пользователя: {}", user);
            throw new DAOException("Ошибка обновления пользователя: " + user, e);
        }
    }

    @Override
    public void delete(User user) {
        log.info("Удаление пользователя: {}", user);
        try(var entityManager = EntityManagerPool.getEntityManager()) {
            entityManager.getTransaction().begin();
            entityManager.remove(user);
            entityManager.getTransaction().commit();
            log.info("Пользователь {} удален", user);
        } catch (IllegalStateException e) {
            log.error("Ошибка удаления пользователя: {}", user);
            throw new DAOException("Ошибка удаления пользователя: " + user, e);
        }
    }
}