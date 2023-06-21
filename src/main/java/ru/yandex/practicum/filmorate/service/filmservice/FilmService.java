package ru.yandex.practicum.filmorate.service.filmservice;

import ru.yandex.practicum.filmorate.model.Film;

import java.util.List;

public interface FilmService {

    Film createFilm(Film film);

    Film getFilm(long id);

    Film updateFilm(Film film);

    void putLike(long id, long userId);

    void deleteLike(long id, long userId);

    List<Film> getTopFilms(int count);

    List<Film> getAllFilms();

    Film deleteFilm(long filmId);
}