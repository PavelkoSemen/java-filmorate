package ru.yandex.practicum.filmorate.service.filmservice;

import lombok.extern.slf4j.Slf4j;
import ru.yandex.practicum.filmorate.dao.FilmRepository;
import ru.yandex.practicum.filmorate.dao.UserRepository;
import ru.yandex.practicum.filmorate.error.*;
import ru.yandex.practicum.filmorate.model.Film;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
public class FilmServiceImpl implements FilmService {
    private final FilmRepository filmRepository;
    private final UserRepository userRepository;

    @Autowired
    public FilmServiceImpl(FilmRepository filmRepository, UserRepository userRepository) {
        this.filmRepository = filmRepository;
        this.userRepository = userRepository;
    }

    @Override
    public Film createFilm(Film film) {
        log.info("Сохранение фильма: {}", film);
        try {
            return filmRepository.save(film).orElseThrow(() -> new SaveFilmException("Фильм не сохранен: " + film));
        } catch (DAOException e) {
            log.error("Ошибка сохранения фильма: {}", film);
            throw new RepositoryException(e);
        }
    }

    @Override
    public Film getFilm(long id) {
        log.info("Получение фильма по id: {}", id);
        try {
            return filmRepository.get(id).orElseThrow(() -> new UnknownFilmException("Фильм не найден: " + id));
        } catch (DAOException e) {
            log.error("Ошибка получения фильма: {}", id);
            throw new RepositoryException(e);
        }
    }

    @Override
    public Film updateFilm(Film film) {
        log.info("Обновление фильма {}", film);
        try {
            return filmRepository.update(film).orElseThrow(() -> new UnknownFilmException("Фильм не найден: " + film));
        } catch (DAOException e) {
            log.error("Ошибка обновления фильма: {}", film);
            throw new RepositoryException(e);
        }
    }

    @Override
    public Film putLike(long id, long userId) {
        filmRepository.get(id).orElseThrow(() ->
                new UnknownFilmException("Фильм не найден: " + id));
        userRepository.get(userId).orElseThrow(() ->
                new UnknownUserException("Пользователь не найден: " + userId));
        return filmRepository.putLike(id, userId);
    }

    @Override
    public void deleteLike(long id, long userId) {
        filmRepository.get(id).orElseThrow(() ->
                new UnknownFilmException("Фильм не найден: " + id));
        userRepository.get(userId).orElseThrow(() ->
                new UnknownUserException("Пользователь не найден: " + userId));
        filmRepository.deleteLike(id, userId);
    }

    @Override
    public List<Film> getTopFilms(int count) {
        return filmRepository.findTopFilms(count);
    }

    @Override
    public List<Film> getAllFilms() {
        log.info("Получение фильмов");
        try {
            return filmRepository.getAll();
        } catch (DAOException e) {
            log.error("Ошибка получения списка фильмов");
            throw new RepositoryException(e);
        }
    }
}