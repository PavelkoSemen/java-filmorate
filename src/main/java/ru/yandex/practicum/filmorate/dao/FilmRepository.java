package ru.yandex.practicum.filmorate.dao;

import ru.yandex.practicum.filmorate.model.Film;

import java.util.List;
import java.util.Optional;

public interface FilmRepository {
    List<Film> getAll();

    Optional<Film> get(long id);

    Optional<Film> save(Film t);

    Optional<Film> update(Film t);

    Film putLike(long filmId, long userId);

    Film deleteLike(long filmId, long userId);

    List<Film> findTopFilms(int countFilms);

    void delete(Film t);
}