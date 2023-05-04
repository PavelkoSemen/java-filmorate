package ru.yandex.practicum.filmorate.dao;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;

// Так как сложно было настроить это в контексте спринга, сделал синглетон.
public class EntityManagerPool {
    private static final EntityManagerFactory entityManagerFactory = Persistence
            .createEntityManagerFactory("persistenceUnit");

    private EntityManagerPool() {
    }

    public static EntityManager getEntityManager() {
        return entityManagerFactory.createEntityManager();
    }
}