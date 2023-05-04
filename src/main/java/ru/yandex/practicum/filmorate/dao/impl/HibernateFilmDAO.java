package ru.yandex.practicum.filmorate.dao.impl;

import lombok.extern.slf4j.Slf4j;
import ru.yandex.practicum.filmorate.dao.DAO;
import ru.yandex.practicum.filmorate.dao.EntityManagerPool;
import ru.yandex.practicum.filmorate.error.DAOException;
import ru.yandex.practicum.filmorate.model.Film;
import org.springframework.stereotype.Repository;

import java.util.*;

@Slf4j
@Repository
public class HibernateFilmDAO implements DAO<Film> {

    @Override
    public List<Film> getAll() {
        log.info("Получение списка всех фильмов");
        try {
            var entityManager = EntityManagerPool.getEntityManager();
            List<Film> films = entityManager.createQuery("FROM Film", Film.class).getResultList();
            entityManager.close();
            return films;
        } catch (IllegalStateException e) {
            log.error("Ошибка получения фильмов");
            throw new DAOException("Ошибка получения фильмов", e);
        }
    }

    @Override
    public Optional<Film> get(long id) {
        log.info("Получение фильма с id: {}", id);
        try {
            var entityManager = EntityManagerPool.getEntityManager();
            Film film = entityManager.find(Film.class, id);
            entityManager.close();
            return Optional.of(film);
        } catch (IllegalStateException e) {
            log.error("Ошибка получения фильма с id: {}", id);
            throw new DAOException("Ошибка получения фильма с id: " + id, e);
        }
    }

    @Override
    public Optional<Film> save(Film film) {
        log.info("Сохранение фильма: {}", film);
        try {
            var entityManager = EntityManagerPool.getEntityManager();
            entityManager.getTransaction().begin();
            entityManager.persist(film);
            entityManager.getTransaction().commit();
            entityManager.close();
            if (film.getId() == 0)
                return Optional.empty();
            log.info("Фильма {} сохранен", film);
            return Optional.of(film);
        } catch (IllegalStateException e) {
            log.error("Ошибка сохранения фильма: {}", film);
            throw new DAOException("Ошибка сохранения фильма: " + film, e);
        }
    }

    //optionalFilm = Optional.of(entityManager.merge(user));
    @Override
    public Optional<Film> update(Film film) {
        log.info("Обновление фильма: {}", film);
        try {
            var entityManager = EntityManagerPool.getEntityManager();
            Optional<Film> optionalFilm = Optional.empty();
            Film modifiedFilm = entityManager.find(Film.class, film.getId());

            if (modifiedFilm != null) {
                optionalFilm = Optional.of(modifiedFilm);
                entityManager.getTransaction().begin();
                modifiedFilm.setName(film.getName());
                modifiedFilm.setDescription(film.getDescription());
                modifiedFilm.setDuration(film.getDuration());
                modifiedFilm.setReleaseDate(film.getReleaseDate());
                entityManager.getTransaction().commit();
                log.info("Фильма {} обновлен", film);
            }
            entityManager.close();
            return optionalFilm;
        } catch (IllegalStateException e) {
            log.error("Ошибка обновления фильма: {}", film);
            throw new DAOException("Ошибка обновления фильма: " + film, e);
        }
    }

    @Override
    public void delete(Film film) {
        log.info("Удаление фильма: {}", film);
        try {
            var entityManager = EntityManagerPool.getEntityManager();
            entityManager.getTransaction().begin();
            entityManager.remove(film);
            entityManager.getTransaction().commit();
            entityManager.close();
            log.info("Фильма {} удален", film);
        } catch (IllegalStateException e) {
            log.error("Ошибка удаления фильма: {}", film);
            throw new DAOException("Ошибка удаления фильма: " + film, e);
        }
    }
}