package ru.yandex.practicum.filmorate.dao;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;
import ru.yandex.practicum.filmorate.model.Director;

import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest
@AutoConfigureTestDatabase
@Transactional
@RequiredArgsConstructor(onConstructor_ = @Autowired)
class DirectorRepositoryTest {

    @Autowired
    DirectorRepository directorRepository;
    @Autowired
    FilmRepository filmRepository;
    private final Director director1 = new Director(1, "Режиссер 1");

    @Test
    @DisplayName("Должен добавить к фильму режиссера, которого еще нет в БД")
    void shouldReturnWithoutBDDirector() {
        directorRepository.addFilmDirector(1, director1);
        assertTrue(filmRepository.get(1).orElseThrow().getDirectors().contains(director1));
    }

    @Test
    @DisplayName("Должен добавить к фильму режиссера, который уже есть в БД")
    void shouldReturnWithBDDirector() {
        directorRepository.addFilmDirector(1, director1);
        directorRepository.addFilmDirector(2, director1);
        assertTrue(filmRepository.get(2).orElseThrow().getDirectors().contains(director1));
    }
}
