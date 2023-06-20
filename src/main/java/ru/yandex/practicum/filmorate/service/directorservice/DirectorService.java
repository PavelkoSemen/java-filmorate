package ru.yandex.practicum.filmorate.service.directorservice;

import ru.yandex.practicum.filmorate.model.Director;

public interface DirectorService {
    Director addFilmDirector(long filmId, Director director);
}
