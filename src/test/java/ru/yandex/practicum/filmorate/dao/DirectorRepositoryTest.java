package ru.yandex.practicum.filmorate.dao;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;
import ru.yandex.practicum.filmorate.model.Director;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest
@AutoConfigureTestDatabase
@Transactional
@RequiredArgsConstructor(onConstructor_ = @Autowired)
class DirectorRepositoryTest {

    @Autowired
    private final DirectorRepository directorRepository;
    private Director director1;
    private Director director2;
    private Director director3;

    @BeforeEach
    void createObjectsForTests() {
        director1 = new Director(1, "Режиссер 1");
        director2 = new Director(2, "Режиссер 2");
        director3 = new Director(3, "Режиссер 3");
    }

    @Test
    @DisplayName("Должен создать и вернуть режиссера")
    void shouldCreateAndReturnDirector() {
        directorRepository.createDirector(director1);
        long id = directorRepository.createDirector(director2).orElseThrow().getId();
        directorRepository.createDirector(director3);
        assertEquals(director2, directorRepository.findDirectorById(id).orElseThrow());
    }


    @Test
    @DisplayName("Должен обновить режиссера")
    void shouldUpdateDirector() {
        directorRepository.createDirector(director1);
        long id = directorRepository.createDirector(director2).orElseThrow().getId();
        director2.setName("Новый режиссер 2");
        directorRepository.updateDirector(director2);
        assertThat(directorRepository.findDirectorById(id)).isPresent().hasValueSatisfying(director ->
                assertThat(director).hasFieldOrPropertyWithValue("name", "Новый режиссер 2"));
    }

    @Test
    @DisplayName("Должен удалить режиссера")
    void shouldDeleteDirector() {
        long id = directorRepository.createDirector(director1).orElseThrow().getId();
        directorRepository.createDirector(director2);
        directorRepository.deleteDirector(id);
        assertEquals(Optional.empty(), directorRepository.findDirectorById(id));
    }

    @Test
    @DisplayName("Должен вернуть всех режиссеров")
    void shouldReturnAllDirectors() {
        director1.setId(directorRepository.createDirector(director1).orElseThrow().getId());
        director2.setId(directorRepository.createDirector(director2).orElseThrow().getId());
        director3.setId(directorRepository.createDirector(director3).orElseThrow().getId());
        List<Director> testDirectors = directorRepository.findAll();
        assertTrue(testDirectors.contains(director1));
        assertTrue(testDirectors.contains(director2));
        assertTrue(testDirectors.contains(director3));
    }
}
