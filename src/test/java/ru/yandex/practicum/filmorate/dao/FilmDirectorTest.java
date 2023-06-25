package ru.yandex.practicum.filmorate.dao;

import lombok.NoArgsConstructor;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;
import ru.yandex.practicum.filmorate.model.Director;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Mpa;

import java.time.LocalDate;
import java.util.LinkedList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@AutoConfigureTestDatabase
@Transactional
@NoArgsConstructor
public class FilmDirectorTest {

    @Autowired
    private FilmRepository filmRepository;

    @Autowired
    private DirectorRepository directorRepository;

    private Director director2;
    private Film film1;
    private Film film2;
    private Film film3;

    @BeforeEach
    void createObjectsForTests() {
        directorRepository.createDirector(new Director(1, "Режиссер 1"));
        director2 = new Director(2, "Режиссер 2");
        director2.setId(directorRepository.createDirector(director2).orElseThrow().getId());
        directorRepository.createDirector(new Director(3, "Режиссер 3"));

        film1 = new Film(1, "Фильм 1", "Описание 1",
                LocalDate.of(2022, 2, 2), 110, new Mpa(1, null, null));
        film2 = new Film(2, "Фильм 2", "Описание 2",
                LocalDate.of(2023, 3, 3), 120, new Mpa(2, null, null));
        film3 = new Film(3, "Фильм 3", "Описание 3",
                LocalDate.of(2021, 1, 1), 130, new Mpa(3, null, null));

        film1.getDirectors().add(director2);
        film2.getDirectors().add(director2);
        film2.getDirectors().add(director2);
        film3.getDirectors().add(director2);

        film1.setId(filmRepository.save(film1).orElseThrow().getId());
        film2.setId(filmRepository.save(film2).orElseThrow().getId());
        film3.setId(filmRepository.save(film3).orElseThrow().getId());
    }

    @Test
    @DisplayName("Должен вернуть сортированный по лайкам список фильмов режиссера")
    void shouldReturnFilmDirectorsSortByLike() {
        filmRepository.putLike(film2.getId(), 1);
        filmRepository.putLike(film2.getId(), 2);
        filmRepository.putLike(film3.getId(), 1);

        List<Film> filmList = new LinkedList<>(filmRepository.findFilmsByDirector(director2.getId(), "likes"));

        assertThat(filmList)
                .element(0)
                .hasFieldOrPropertyWithValue("id", film2.getId());
        assertThat(filmList)
                .element(1)
                .hasFieldOrPropertyWithValue("id", film3.getId());
        assertThat(filmList)
                .element(2)
                .hasFieldOrPropertyWithValue("id", film1.getId());
    }

    @Test
    @DisplayName("Должен вернуть сортированный по годам список фильмов режиссера")
    void shouldReturnFilmDirectorsSortByYear() {
        List<Film> filmList = new LinkedList<>(filmRepository.findFilmsByDirector(director2.getId(), "year"));

        assertThat(filmList)
                .element(0)
                .hasFieldOrPropertyWithValue("id", film3.getId());
        assertThat(filmList)
                .element(1)
                .hasFieldOrPropertyWithValue("id", film1.getId());
        assertThat(filmList)
                .element(2)
                .hasFieldOrPropertyWithValue("id", film2.getId());
    }
}
