package ru.yandex.practicum.filmorate.service.genreservice;

import ru.yandex.practicum.filmorate.model.Genre;

import java.util.List;

public interface GenreService {
    Genre getGenre(long id);

    List<Genre> getAllGenres();
}