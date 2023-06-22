package ru.yandex.practicum.filmorate.dao;

import ru.yandex.practicum.filmorate.model.Director;

import java.util.List;
import java.util.Optional;

public interface DirectorRepository {

    Optional<Director> getDirector(long id);

    Optional<Director>  createDirector(Director director);

    Optional<Director> updateDirector(Director director);

    void deleteDirector(long id);

    List<Director> getAllDirectors();
}