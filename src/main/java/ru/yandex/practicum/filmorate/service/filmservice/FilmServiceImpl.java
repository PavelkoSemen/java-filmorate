package ru.yandex.practicum.filmorate.service.filmservice;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.dao.FilmRepository;
import ru.yandex.practicum.filmorate.dao.UserRepository;
import ru.yandex.practicum.filmorate.error.EntityNotFoundException;
import ru.yandex.practicum.filmorate.error.EntitySaveException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.model.eventenum.EventOperation;
import ru.yandex.practicum.filmorate.model.eventenum.EventType;
import ru.yandex.practicum.filmorate.utils.customannotations.EventFeed;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

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
        return filmRepository.save(film).orElseThrow(() -> new EntitySaveException("Фильм не сохранен: " + film));
    }

    @Override
    public Film getFilm(long id) {
        log.info("Получение фильма по id: {}", id);

        return filmRepository.findFilmById(id).orElseThrow(() -> new EntityNotFoundException("Фильм не найден: " + id));
    }


    @Override
    public Film updateFilm(Film film) {
        log.info("Обновление фильма {}", film);
        return filmRepository.update(film).orElseThrow(() -> new EntityNotFoundException("Фильм не найден: " + film));
    }

    @Override
    @EventFeed(operation = EventOperation.ADD, type = EventType.LIKE)
    public boolean putLike(long id, long userId) {
        filmRepository.findFilmById(id).orElseThrow(() ->
                new EntityNotFoundException("Фильм не найден: " + id));
        userRepository.findUserById(userId).orElseThrow(() ->
                new EntityNotFoundException("Пользователь не найден: " + userId));
        return filmRepository.putLike(id, userId);

    }

    @Override
    @EventFeed(operation = EventOperation.REMOVE, type = EventType.LIKE)
    public boolean deleteLike(long id, long userId) {
        filmRepository.findFilmById(id).orElseThrow(() ->
                new EntityNotFoundException("Фильм не найден: " + id));
        userRepository.findUserById(userId).orElseThrow(() ->
                new EntityNotFoundException("Пользователь не найден: " + userId));
        return filmRepository.deleteLike(id, userId);
    }

    @Override
    public List<Film> getTopFilms(int count, Long genreId, Integer year) {
        return filmRepository.findTopFilmsWithLimit(count).stream()
                .filter(film -> genreId == null || film.getGenres()
                        .stream()
                        .map(Genre::getId)
                        .anyMatch(id -> id.equals(genreId)))
                .filter(film -> year == null || film.getReleaseDate().getYear() == year)
                .collect(Collectors.toList());

    }

    @Override
    public List<Film> getMutualTopFilms(long userId, long friendId) {
        log.info("Получение общих фильмов для пользователей {} {}", userId, friendId);
        List<Film> mutualFilms = new ArrayList<>();

        List<Film> filmsUser = filmRepository.findTopFilmsByUserId(userId);
        List<Film> filmsFriend = filmRepository.findTopFilmsByUserId(friendId);

        for (Film film : filmsUser) {
            if (filmsFriend.contains(film)) {
                mutualFilms.add(film);
            }
        }
        return mutualFilms;
    }

    @Override
    public List<Film> getAllFilms() {
        log.info("Получение фильмов");
        return filmRepository.findAll();
    }

    @Override
    public Film deleteFilm(long id) {
        log.info("Удаление фильма по id: {}:", id);
        var film = getFilm(id);
        filmRepository.delete(film);
        return film;
    }

    @Override
    public List<Film> getFilmsByFilter(String query, String by) {
        return filmRepository.findFilmsByFilter(query, by);
    }

    @Override
    public List<Film> getFilmsByDirector(long directorId, String sortBy) {
        log.info("Получение всех фильмов от режиссера с id: {}. Дополнительное условие {}", directorId, sortBy);
        List<Film> filmList = filmRepository.findFilmsByDirector(directorId, sortBy);
        if (filmList.size() == 0) {
            throw new EntityNotFoundException("У режиссера с id: " + directorId + " нет фильмов");
        }
        return filmList;
    }
}