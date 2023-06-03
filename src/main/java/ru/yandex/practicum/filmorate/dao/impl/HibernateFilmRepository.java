package ru.yandex.practicum.filmorate.dao.impl;

import lombok.extern.slf4j.Slf4j;
import ru.yandex.practicum.filmorate.dao.EntityManagerPool;
import ru.yandex.practicum.filmorate.dao.FilmRepository;
import ru.yandex.practicum.filmorate.model.Film;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.model.User;

import java.util.*;

@Slf4j
@Repository
public class HibernateFilmRepository implements FilmRepository {

    @Override
    public List<Film> getAll() {
        log.info("Получение списка всех фильмов");
        var entityManager = EntityManagerPool.getEntityManager();
        List<Film> films = entityManager.createQuery("FROM Film", Film.class).getResultList();
        entityManager.close();
        return films;
    }

    @Override
    public Optional<Film> get(long id) {
        log.info("Получение фильма с id: {}", id);
        var entityManager = EntityManagerPool.getEntityManager();
        Film film = entityManager.find(Film.class, id);
        entityManager.close();
        return Optional.ofNullable(film);
    }

    @Override
    public Optional<Film> save(Film film) {
        log.info("Сохранение фильма: {}", film);
        var entityManager = EntityManagerPool.getEntityManager();
        entityManager.getTransaction().begin();
        entityManager.persist(film);
        entityManager.getTransaction().commit();
        entityManager.close();
        if (film.getId() == 0)
            return Optional.empty();
        log.info("Фильма {} сохранен", film);
        return Optional.of(film);
    }

    @Override
    public Optional<Film> update(Film film) {
        log.info("Обновление фильма: {}", film);
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
    }

    @Override
    public Film putLike(long filmId, long userId) {
        log.info("Добавить фильму {} лайк, от пользователя {}", filmId, userId);
        var entityManager = EntityManagerPool.getEntityManager();
        entityManager.getTransaction().begin();
        Film film = entityManager.find(Film.class, filmId);
        User user = entityManager.find(User.class, userId);
        film.addUser(user);
        entityManager.getTransaction().commit();
        return film;
    }

    @Override
    public Film deleteLike(long filmId, long userId) {
        log.info("Удалить у фильма {} лайк, от пользователя {}", filmId, userId);
        var entityManager = EntityManagerPool.getEntityManager();
        entityManager.getTransaction().begin();
        Film film = entityManager.find(Film.class, filmId);
        User user = entityManager.find(User.class, userId);
        film.removeUser(user);
        return film;
    }

    @Override
    public List<Film> findTopFilms(int countFilms) {
        log.info("Вернуть топ {} фильмов", countFilms);
        var entityManager = EntityManagerPool.getEntityManager();
        entityManager.getTransaction().begin();
        @SuppressWarnings("unchecked") List<Film> films = entityManager
                .createNativeQuery("SELECT f.* FROM films f " +
                                "LEFT JOIN (SELECT film_id " +
                                "                   ,COUNT(user_id) as count_likes " +
                                "              FROM likes " +
                                "              GROUP BY film_id) cf " +
                                "    ON cf.film_id= f.id " +
                                "ORDER BY count_likes desc",
                        Film.class)
                .setMaxResults(countFilms)
                .getResultList();
        entityManager.getTransaction().commit();
        entityManager.close();
        return films;
    }

    @Override
    public void delete(Film film) {
        log.info("Удаление фильма: {}", film);
        var entityManager = EntityManagerPool.getEntityManager();
        entityManager.getTransaction().begin();
        entityManager.remove(film);
        entityManager.getTransaction().commit();
        entityManager.close();
        log.info("Фильма {} удален", film);
    }
}