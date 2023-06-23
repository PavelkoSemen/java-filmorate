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
    public List<Film> getTopFilms(int count, Long genreId, Integer year) {
        if (genreId != null && year != null) {
            List<Film> listByGenreByYear = new ArrayList<>();
            filmRepository.findTopFilmsWithLimit(count)
                    .forEach(film -> film.getGenres()
                            .stream()
                            .filter(genre -> genre.getId() == genreId && film.getReleaseDate().getYear() == year)
                            .map(genre -> film).forEach(listByGenreByYear::add));
            return listByGenreByYear;
        } else if (genreId != null) {
            List<Film> listByGenre = new ArrayList<>();
            filmRepository.findTopFilmsWithLimit(count)
                    .forEach(film -> film.getGenres()
                            .stream()
                            .filter(genre -> genre.getId() == genreId)
                            .map(genre -> film).forEach(listByGenre::add));
            return listByGenre;
        } else if (year != null) {
            return filmRepository.findTopFilmsWithLimit(count).stream()
                    .filter(film -> film.getReleaseDate().getYear() == year)
                    .collect(Collectors.toList());
        } else {
            return filmRepository.findTopFilmsWithLimit(count);
        }
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
        return filmRepository.getAll();
    }

    @Override
    public Film deleteFilm(long id) {
        log.info("Удаление фильма по id: {}:", id);
        var film = getFilm(id);
        filmRepository.delete(film);
        return film;
    }

    @Override
    public List<Film> search(String query, String by) {
        return filmRepository.search(query, by);
    }

    @Override
    public List<Film> getFilmsByDirector(long directorId, String sortBy) {
        log.info("Получение всех фильмов от режиссера с id: {}. Дополнительное условие {}", directorId, sortBy);
        return filmRepository.getFilmsByDirector(directorId, sortBy);
    }
}