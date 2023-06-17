package ru.yandex.practicum.filmorate.dao;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;
import ru.yandex.practicum.filmorate.model.Mpa;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@AutoConfigureTestDatabase
@Transactional
@RequiredArgsConstructor(onConstructor_ = @Autowired)
class MpaRepositoryTest {

    @Autowired
    MpaRepository mpaRepository;

    @Test
    @DisplayName("Должен вернуть возрастной рейтинг")
    void shouldReturnMpaById() {
        Optional<Mpa> optionalMpa = mpaRepository.get(1);
        assertThat(optionalMpa).isPresent().hasValueSatisfying(mpa ->
                assertThat(mpa).hasFieldOrPropertyWithValue("id", 1L));
    }

    @Test
    @DisplayName("Должен вернуть список возрастных рейтингов")
    void shouldReturnAllMpa() {
        List<Mpa> mpaList = mpaRepository.getAll();
        assertThat(mpaList).hasSize(5);
    }
}