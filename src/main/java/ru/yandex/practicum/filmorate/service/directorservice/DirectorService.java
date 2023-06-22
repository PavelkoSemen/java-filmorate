package ru.yandex.practicum.filmorate.service.directorservice;

import ru.yandex.practicum.filmorate.model.Director;

import java.util.List;

public interface DirectorService {

    Director getDirector(long id);

    Director createDirector(Director director);

    Director updateDirector(Director director);

    void deleteDirector(long id);

    List<Director> getAllDirectors();
}