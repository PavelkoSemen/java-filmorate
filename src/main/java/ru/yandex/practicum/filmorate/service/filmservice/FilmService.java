package ru.yandex.practicum.filmorate.service.filmservice;

import ru.yandex.practicum.filmorate.model.Film;

import java.util.List;

public interface FilmService {

    Film createFilm(Film film);

    Film getFilm(long id);

    Film updateFilm(Film film);

    void deleteFilm(long id);


    List<Film> getAllFilms();
}