package ru.yandex.practicum.filmorate.dao;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

public class EntityManagerPool {
    private static final EntityManagerFactory entityManagerFactory = Persistence
            .createEntityManagerFactory("persistenceUnit");

    private EntityManagerPool() {
    }

    public static EntityManager getEntityManager() {
        return entityManagerFactory.createEntityManager();
    }
}