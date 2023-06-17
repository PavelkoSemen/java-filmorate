package ru.yandex.practicum.filmorate.dao.hibernateimpl;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.dao.MpaRepository;
import ru.yandex.practicum.filmorate.model.Mpa;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.List;
import java.util.Optional;

@Repository
@Slf4j
public class HibernateMpaRepository implements MpaRepository {
    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public Optional<Mpa> get(long id) {
        log.info("Возрастное ограничение с id: {}", id);
        Mpa mpa = entityManager.find(Mpa.class, id);
        return Optional.ofNullable(mpa);
    }

    @Override
    public List<Mpa> getAll() {
        log.info("Получение списка всех возрастных ограничений");
        return entityManager.createQuery("FROM Mpa", Mpa.class).getResultList();
    }
}