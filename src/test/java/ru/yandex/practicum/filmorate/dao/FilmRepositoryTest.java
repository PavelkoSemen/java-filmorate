package ru.yandex.practicum.filmorate.dao;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Mpa;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@AutoConfigureTestDatabase
@Transactional
@RequiredArgsConstructor(onConstructor_ = @Autowired)
class FilmRepositoryTest {

    @Autowired
    FilmRepository filmRepository;
    private Film firstFilm;

    @BeforeEach
    void setUp() {
        Mpa mpa = new Mpa();
        mpa.setId(1);

        firstFilm = new Film();
        firstFilm.setName("name4");
        firstFilm.setDescription("description4");
        firstFilm.setReleaseDate(LocalDate.of(1900, 3, 25));
        firstFilm.setDuration(200);
        firstFilm.setMpa(mpa);
    }

    @Test
    @DisplayName("Должен вернуть список фильмов")
    void shouldReturnAllFilms() {
        List<Film> films = filmRepository.getAll();
        assertThat(films).hasSize(3);
    }

    @Test
    @DisplayName("Должен вернуть фильм по id")
    void shouldReturnFilmById() {
        Optional<Film> optionalFilm = filmRepository.get(1);
        assertThat(optionalFilm)
                .isPresent()
                .hasValueSatisfying(film ->
                        assertThat(film).hasFieldOrPropertyWithValue("id", 1L));
    }

    @Test
    @DisplayName("Должен сохранить и вернуть фильм")
    void shouldSaveAndReturnFilm() {
        Optional<Film> optionalFilm = filmRepository.save(firstFilm);
        assertThat(optionalFilm)
                .isPresent()
                .hasValueSatisfying(film ->
                        assertThat(film).hasFieldOrPropertyWithValue("id", 4L));
    }

    @Test
    @DisplayName("Должен обновить и вернуть фильм")
    void shouldUpdateAndReturnFilm() {
        firstFilm.setId(3);
        firstFilm.setName("name5");
        Optional<Film> optionalFilm = filmRepository.update(firstFilm);
        assertThat(optionalFilm)
                .isPresent()
                .contains(firstFilm);

    }

    @Test
    @DisplayName("Должен добавить лайк фильму")
    void shouldPutLike() {
        filmRepository.putLike(1, 1);
        List<Film> newTopFilms = filmRepository.findTopFilms(1);
        assertThat(newTopFilms).hasSize(1)
                .element(0)
                .hasFieldOrPropertyWithValue("id", 1L);
    }

    @Test
    @DisplayName("Должен удалить лайк у фильма")
    void shouldDeleteLike() {
        filmRepository.putLike(1, 1);
        List<Film> oldTopFilms = filmRepository.findTopFilms(1);
        assertThat(oldTopFilms).hasSize(1)
                .element(0)
                .hasFieldOrPropertyWithValue("id", 1L);

        filmRepository.deleteLike(1, 1);
        filmRepository.putLike(2, 1);

        List<Film> newTopFilms = filmRepository.findTopFilms(1);
        assertThat(newTopFilms).hasSize(1)
                .element(0)
                .hasFieldOrPropertyWithValue("id", 2L);
    }

    @Test
    @DisplayName("Должен вернуть топ фильмов")
    void shouldReturnTopFilms() {
        filmRepository.putLike(1, 1);
        List<Film> topFilms = filmRepository.findTopFilms(1);
        assertThat(topFilms).hasSize(1)
                .element(0)
                .hasFieldOrPropertyWithValue("id", 1L);

        filmRepository.putLike(2, 1);
        filmRepository.putLike(2, 2);

        List<Film> topFilmsSecond = filmRepository.findTopFilms(1);
        assertThat(topFilmsSecond).hasSize(1)
                .element(0)
                .hasFieldOrPropertyWithValue("id", 2L);
    }
}