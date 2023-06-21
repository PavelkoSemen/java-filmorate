package ru.yandex.practicum.filmorate.dao;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.model.Mpa;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;

@SpringBootTest
@AutoConfigureTestDatabase
@RequiredArgsConstructor(onConstructor_ = @Autowired)
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
class FilmDeletingTests {
    private final int offset = 3;
    private final FilmRepository filmStorage;

    private void createFilm() {
        var testFilm = new Film();
        testFilm.setName("name");
        testFilm.setDuration(111);
        var date = LocalDate.parse("20230101", DateTimeFormatter.BASIC_ISO_DATE);
        testFilm.setReleaseDate(date);
        testFilm.setDescription("d1");
        var mpa = new Mpa();
        mpa.setId(1);
        testFilm.setMpa(mpa);
        var genre = new Genre();
        genre.setId(1);
        testFilm.setGenres(new HashSet<>(List.of(genre)));
        filmStorage.save(testFilm);
    }

    private void createFilm1() {
        var testFilm = new Film();
        testFilm.setName("name2");
        testFilm.setDuration(222);
        var date = LocalDate.parse("20230101", DateTimeFormatter.BASIC_ISO_DATE);
        testFilm.setReleaseDate(date);
        testFilm.setDescription("d2");
        var mpa = new Mpa();
        mpa.setId(2);
        testFilm.setMpa(mpa);
        filmStorage.save(testFilm);
    }

    private void checkFilm(Optional<Film> filmOpt) {
        var film = filmOpt.get();
        var date = LocalDate.parse("20230101", DateTimeFormatter.BASIC_ISO_DATE);
        Assertions.assertEquals("name", film.getName());
        Assertions.assertEquals("d1", film.getDescription());
        Assertions.assertEquals(111, film.getDuration());
        Assertions.assertEquals(date, film.getReleaseDate());
        Assertions.assertEquals(1, film.getMpa().getId());
        Assertions.assertEquals("G", film.getMpa().getName());
    }

    private void checkFilm1(Optional<Film> filmOpt) {
        var film = filmOpt.get();
        var date = LocalDate.parse("20230101", DateTimeFormatter.BASIC_ISO_DATE);
        Assertions.assertEquals("name2", film.getName());
        Assertions.assertEquals("d2", film.getDescription());
        Assertions.assertEquals(222, film.getDuration());
        Assertions.assertEquals(date, film.getReleaseDate());
        Assertions.assertEquals(2, film.getMpa().getId());
        Assertions.assertEquals("PG", film.getMpa().getName());
    }

    @Test
    void testFindFilmById() {
        createFilm();
        var film = filmStorage.get(1 + offset);
        checkFilm(film);
    }

    @Test
    void testFindFilm1ById() {
        createFilm1();
        var film = filmStorage.get(1 + offset);
        checkFilm1(film);
    }

    @Test
    void testGetFilms() {
        createFilm();
        createFilm1();
        var films = filmStorage.getAll();
        Assertions.assertEquals(2 + offset, films.size());
        checkFilm(films.stream().skip(offset).findFirst());
        checkFilm1(films.stream().skip(offset + 1).findFirst());
    }

    @Test
    void testDeleteFilm() {
        createFilm();
        var film = filmStorage.get(1 + offset);
        filmStorage.delete(film.orElse(null));
        var films = filmStorage.getAll();
        Assertions.assertEquals(offset, films.size());
    }

    @Test
    void testAdd2FilmsDelete1Film() {
        createFilm();
        createFilm1();
        var film = filmStorage.get(1 + offset);
        filmStorage.delete(film.orElse(null));
        var films = filmStorage.getAll();
        checkFilm1(films.stream().skip(offset).findFirst());
    }

    @Test
    void topFilmsTest() {
        createFilm();
        createFilm1();
        filmStorage.putLike(1 + offset, 1);
        filmStorage.putLike(2 + offset, 3);
        filmStorage.putLike(2 + offset, 2);

        var films = filmStorage.findTopFilmsWithLimit(1);
        checkFilm1(films.stream().findFirst());
    }
}