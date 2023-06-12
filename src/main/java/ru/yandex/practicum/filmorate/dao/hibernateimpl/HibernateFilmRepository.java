package ru.yandex.practicum.filmorate.dao.hibernateimpl;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.dao.FilmRepository;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.User;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.transaction.Transactional;
import java.util.List;
import java.util.Optional;

@Slf4j
@Repository
public class HibernateFilmRepository implements FilmRepository {

    @PersistenceContext
    private EntityManager entityManager;

    public HibernateFilmRepository(EntityManager entityManager) {
        this.entityManager = entityManager;
    }

    @Override
    public List<Film> getAll() {
        log.info("Получение списка всех фильмов");
        return entityManager.createQuery("FROM Film", Film.class).getResultList();
    }

    @Override
    public Optional<Film> get(long id) {
        log.info("Получение фильма с id: {}", id);
        Film film = entityManager.find(Film.class, id);
        return Optional.ofNullable(film);
    }

    @Override
    @Transactional
    public Optional<Film> save(Film film) {
        log.info("Сохранение фильма: {}", film);
        entityManager.persist(film);
        if (film.getId() == 0)
            return Optional.empty();
        log.info("Фильма {} сохранен", film);
        return Optional.of(film);
    }

    @Override
    @Transactional
    public Optional<Film> update(Film film) {
        log.info("Обновление фильма: {}", film);
        Optional<Film> optionalFilm = Optional.empty();
        Film modifiedFilm = entityManager.find(Film.class, film.getId());
        if (modifiedFilm != null) {
            optionalFilm = Optional.of(modifiedFilm);
            modifiedFilm.setName(film.getName());
            modifiedFilm.setDescription(film.getDescription());
            modifiedFilm.setDuration(film.getDuration());
            modifiedFilm.setReleaseDate(film.getReleaseDate());
            modifiedFilm.setMpa(film.getMpa());
            modifiedFilm.setGenres(film.getGenres());
            log.info("Фильма {} обновлен", film);
        }
        return optionalFilm;
    }

    @Override
    @Transactional
    public void putLike(long filmId, long userId) {
        log.info("Добавить фильму {} лайк, от пользователя {}", filmId, userId);
        Film film = entityManager.find(Film.class, filmId);
        User user = entityManager.find(User.class, userId);
        film.addUser(user);
        log.info("Добавлен лайк фильму {} , от пользователя {}", filmId, userId);
    }

    @Override
    @Transactional
    public void deleteLike(long filmId, long userId) {
        log.info("Удалить у фильма {} лайк, от пользователя {}", filmId, userId);
        Film film = entityManager.find(Film.class, filmId);
        User user = entityManager.find(User.class, userId);
        film.removeUser(user);
        log.info("Удален лайк у фильма {} , от пользователя {}", filmId, userId);
    }

    @Override
    public List<Film> findTopFilms(int countFilms) {
        log.info("Вернуть топ {} фильмов", countFilms);
        @SuppressWarnings("unchecked") List<Film> films = entityManager
                .createNativeQuery("SELECT f.* FROM films f " +
                                "LEFT JOIN (SELECT film_id " +
                                "                   ,COUNT(user_id) as count_likes " +
                                "              FROM likes " +
                                "              GROUP BY film_id) cf " +
                                "    ON cf.film_id= f.film_id " +
                                "ORDER BY count_likes desc",
                        Film.class)
                .setMaxResults(countFilms)
                .getResultList();
        return films;
    }

    @Override
    @Transactional
    public void delete(Film film) {
        log.info("Удаление фильма: {}", film);
        entityManager.remove(film);
        log.info("Фильма {} удален", film);
    }
}