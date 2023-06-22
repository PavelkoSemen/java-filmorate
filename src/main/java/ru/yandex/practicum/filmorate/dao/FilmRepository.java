package ru.yandex.practicum.filmorate.dao;

import ru.yandex.practicum.filmorate.model.Film;

import java.util.List;
import java.util.Optional;

public interface FilmRepository {
    List<Film> getAll();

    Optional<Film> get(long id);

    Optional<Film> save(Film t);

    Optional<Film> update(Film t);

    void putLike(long filmId, long userId);

    void deleteLike(long filmId, long userId);

    List<Film> findTopFilms();

    List<Film> findTopFilmsWithLimit(int countFilms);
    List<Film> findTopFilmsByUserId(long userId);

    List<Film> getFilmsByDirector(long directorId, String sortBy);

    void delete(Film t);
}