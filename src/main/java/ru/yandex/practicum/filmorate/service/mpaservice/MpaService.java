package ru.yandex.practicum.filmorate.service.mpaservice;

import ru.yandex.practicum.filmorate.model.Mpa;

import java.util.List;

public interface MpaService {
    Mpa getMpa(long id);

    List<Mpa> getAllMpa();
}