package ru.yandex.practicum.filmorate.dao;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.util.Objects;

@SpringBootTest
@AutoConfigureTestDatabase
@Transactional
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class RecommendationTests {
    @Autowired
    private UserRepository userRepository;
    @Autowired
    FilmRepository filmRepository;

    @Test
    void recommendationsTest() {
        filmRepository.putLike(1, 1);
        filmRepository.putLike(2, 1);
        filmRepository.putLike(2, 2);
        var films = userRepository.getRecommendations(2);
        Assertions.assertEquals(1, films.size());
        Assertions.assertEquals(1, Objects.requireNonNull(films.stream().findFirst().orElse(null)).getId());
    }

    @Test
    void emptyRecommendationsTest() {
        filmRepository.putLike(1, 1);
        filmRepository.putLike(2, 1);
        filmRepository.putLike(1, 2);
        filmRepository.putLike(2, 2);
        var films = userRepository.getRecommendations(2);
        Assertions.assertEquals(0, films.size());
    }
}
