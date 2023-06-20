package ru.yandex.practicum.filmorate.dao;

import ru.yandex.practicum.filmorate.model.Director;

import java.util.Optional;

public interface DirectorRepository {
    Optional<Director> addFilmDirector(long filmId, Director director);
}
