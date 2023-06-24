package ru.yandex.practicum.filmorate.dao;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;
import ru.yandex.practicum.filmorate.model.Genre;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@AutoConfigureTestDatabase
@Transactional
@RequiredArgsConstructor(onConstructor_ = @Autowired)
class GenreRepositoryTest {

    @Autowired
    GenreRepository genreRepository;

    @Test
    @DisplayName("Должен вернуть жанр")
    void shouldReturnGenreById() {
        Optional<Genre> optionalGenre = genreRepository.findGenreById(1);
        assertThat(optionalGenre).isPresent().hasValueSatisfying(genre ->
                assertThat(genre).hasFieldOrPropertyWithValue("id", 1L));
    }

    @Test
    @DisplayName("Должен вернуть список жанров")
    void shouldReturnGenreAllGenre() {
        List<Genre> genres = genreRepository.findAll();
        assertThat(genres).hasSize(6);
    }
}