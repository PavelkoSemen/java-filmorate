package ru.yandex.practicum.filmorate.service.filmservice;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.dao.FilmRepository;
import ru.yandex.practicum.filmorate.dao.UserRepository;
import ru.yandex.practicum.filmorate.error.SaveFilmException;
import ru.yandex.practicum.filmorate.error.UnknownFilmException;
import ru.yandex.practicum.filmorate.error.UnknownUserException;
import ru.yandex.practicum.filmorate.model.Film;

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
        return filmRepository.save(film).orElseThrow(() -> new SaveFilmException("Фильм не сохранен: " + film));
    }

    @Override
    public Film getFilm(long id) {
        log.info("Получение фильма по id: {}", id);

        return filmRepository.get(id).orElseThrow(() -> new UnknownFilmException("Фильм не найден: " + id));

    }

    @Override
    public Film updateFilm(Film film) {
        log.info("Обновление фильма {}", film);
        return filmRepository.update(film).orElseThrow(() -> new UnknownFilmException("Фильм не найден: " + film));
    }

    @Override
    public void putLike(long id, long userId) {
        filmRepository.get(id).orElseThrow(() ->
                new UnknownFilmException("Фильм не найден: " + id));
        userRepository.get(userId).orElseThrow(() ->
                new UnknownUserException("Пользователь не найден: " + userId));
        filmRepository.putLike(id, userId);
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
        return filmRepository.getAll();
    }
}