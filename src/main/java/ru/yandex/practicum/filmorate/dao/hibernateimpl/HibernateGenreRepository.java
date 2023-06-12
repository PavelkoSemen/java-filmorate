package ru.yandex.practicum.filmorate.dao.hibernateimpl;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.dao.GenreRepository;
import ru.yandex.practicum.filmorate.model.Genre;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.List;
import java.util.Optional;

@Repository
@Slf4j
public class HibernateGenreRepository implements GenreRepository {
    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public Optional<Genre> get(long id) {
        log.info("Получение жанра с id: {}", id);
        return Optional.ofNullable(entityManager.find(Genre.class, id));
    }

    @Override
    public List<Genre> getAll() {
        log.info("Получение списка всех жанров");
        return entityManager.createQuery("FROM Genre", Genre.class).getResultList();
    }
}