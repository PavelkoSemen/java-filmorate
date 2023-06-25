package ru.yandex.practicum.filmorate.dao;

import ru.yandex.practicum.filmorate.model.Film;

import java.util.List;
import java.util.Optional;

public interface FilmRepository {
    List<Film> findAll();

    Optional<Film> findFilmById(long id);

    Optional<Film> save(Film t);

    Optional<Film> update(Film t);

    boolean putLike(long filmId, long userId);

    boolean deleteLike(long filmId, long userId);

    List<Film> findTopFilms();

    List<Film> findTopFilmsWithLimit(int countFilms);

    List<Film> findTopFilmsByUserId(long userId);

    List<Film> findFilmsByDirector(long directorId, String sortBy);

    void delete(Film t);

    List<Film> findFilmsByFilter(String query, String by);
}