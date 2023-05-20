package ru.yandex.practicum.filmorate.service.filmservice;

import lombok.extern.slf4j.Slf4j;
import ru.yandex.practicum.filmorate.dao.DAO;
import ru.yandex.practicum.filmorate.error.DAOException;
import ru.yandex.practicum.filmorate.error.RepositoryException;
import ru.yandex.practicum.filmorate.error.SaveFilmException;
import ru.yandex.practicum.filmorate.error.UnknownFilmException;
import ru.yandex.practicum.filmorate.model.Film;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
public class FilmServiceImpl implements FilmService {
    private final DAO<Film> filmDAO;

    @Autowired
    public FilmServiceImpl(DAO<Film> filmDAO) {
        this.filmDAO = filmDAO;
    }

    @Override
    public Film createFilm(Film film) {
        log.info("Сохранение фильма: {}", film);
        try {
            return filmDAO.save(film).orElseThrow(() -> new SaveFilmException("Фильм не сохранен: " + film));
        } catch (DAOException e) {
            log.error("Ошибка сохранения фильма: {}", film);
            throw new RepositoryException(e);
        }
    }

    @Override
    public Film getFilm(long id) {
        log.info("Получение фильма по id: {}", id);
        try {
            return filmDAO.get(id).orElseThrow(() -> new UnknownFilmException("Фильм не найден: " + id));
        } catch (DAOException e) {
            log.error("Ошибка получения фильма: {}", id);
            throw new RepositoryException(e);
        }
    }

    @Override
    public Film updateFilm(Film film) {
        log.info("Обновление фильма {}", film);
        try {
            return filmDAO.update(film).orElseThrow(() -> new UnknownFilmException("Фильм не найден: " + film));
        } catch (DAOException e) {
            log.error("Ошибка обновления фильма: {}", film);
            throw new RepositoryException(e);
        }
    }

    @Override
    public void deleteFilm(long id) {

    }

    @Override
    public List<Film> getAllFilms() {
        log.info("Получение фильмов");
        try {
            return filmDAO.getAll();
        } catch (DAOException e) {
            log.error("Ошибка получения списка фильмов");
            throw new RepositoryException(e);
        }
    }
}