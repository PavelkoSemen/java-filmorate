package ru.yandex.practicum.filmorate.dao.impl;

import ru.yandex.practicum.filmorate.dao.DAO;
import ru.yandex.practicum.filmorate.model.Film;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class HibernateFilmDAOTest {

    @Autowired
    DAO<Film> filmDAO;

    @Test
    void save() {
        Film film = new Film();
        film.setName("sdsds");
        film.setDescription("Wsrq");

        filmDAO.save(film);


        System.out.println(film);
    }
}