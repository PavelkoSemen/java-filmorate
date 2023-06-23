package ru.yandex.practicum.filmorate.service.filmservice;

import ru.yandex.practicum.filmorate.model.Film;

import java.util.List;

public interface FilmService {

    Film createFilm(Film film);

    Film getFilm(long id);

    Film updateFilm(Film film);

    boolean putLike(long id, long userId);

    boolean deleteLike(long id, long userId);

    List<Film> getTopFilms(int count, Long genreId, Integer year);

    List<Film> getMutualTopFilms(long userId, long friendId);

    List<Film> getAllFilms();

    Film deleteFilm(long filmId);

    List<Film> getFilmsByDirector(long directorId, String sortBy);

    List<Film> search(String query, String by);
}