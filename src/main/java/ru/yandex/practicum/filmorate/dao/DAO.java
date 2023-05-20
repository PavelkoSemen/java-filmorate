package ru.yandex.practicum.filmorate.dao;

import java.util.List;
import java.util.Optional;

public interface DAO<T> {
    List<T> getAll();

    Optional<T> get(long id);

    Optional<T> save(T t);

    Optional<T> update(T t);

    void delete(T t);
}