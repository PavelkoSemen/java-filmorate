package ru.yandex.practicum.filmorate.dao;

import ru.yandex.practicum.filmorate.model.Event;

import java.util.List;
import java.util.Optional;

public interface EventRepository {
    Optional<Event> save(Event event);

    Optional<Event> findEventById(long id);

    List<Event> findAll(long userId);
}