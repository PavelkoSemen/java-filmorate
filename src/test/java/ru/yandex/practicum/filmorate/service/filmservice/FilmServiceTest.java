package ru.yandex.practicum.filmorate.service.filmservice;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.yandex.practicum.filmorate.dao.FilmRepository;
import ru.yandex.practicum.filmorate.error.EntityNotFoundException;
import ru.yandex.practicum.filmorate.error.EntitySaveException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Mpa;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class FilmServiceTest {

    @Mock
    private FilmRepository filmRepository;

    @InjectMocks
    private FilmServiceImpl filmService;

    private Film firstFilm;
    private Film secondFilm;

    @BeforeEach
    void setUp() {
        Mpa mpaFirst = new Mpa();
        mpaFirst.setId(1);
        Mpa mpaSecond = new Mpa();
        mpaSecond.setId(2);
        firstFilm = new Film(1,
                "film1",
                "film1",
                LocalDate.of(2005, 3, 25),
                100,
                mpaFirst);
        secondFilm = new Film(2,
                "film2",
                "film2",
                LocalDate.of(2015, 3, 25),
                100,
                mpaSecond);
    }


    @DisplayName("Должен сохранить и вернуть фильм")
    @Test
    public void shouldSaveAndReturnTheMovie() {
        given(filmRepository.save(firstFilm)).willReturn(Optional.of(firstFilm));

        Film actualFilm = filmService.createFilm(firstFilm);

        assertEquals(firstFilm, actualFilm);

        verify(filmRepository, times(1)).save(firstFilm);
    }

    @DisplayName("Должен выкинуть исключение при сохранении")
    @Test
    public void shouldThrowAnExceptionWhenSavingAMovie() {
        given(filmRepository.save(firstFilm)).willReturn(Optional.empty());

        assertThrows(EntitySaveException.class,
                () -> filmService.createFilm(firstFilm));

        verify(filmRepository, times(1)).save(firstFilm);
    }


    @DisplayName("Должен обновить и вернуть фильм")
    @Test
    public void shouldUpdateAndReturnTheMovie() {
        given(filmRepository.update(firstFilm)).willReturn(Optional.of(firstFilm));

        Film actualFilm = filmService.updateFilm(firstFilm);

        assertEquals(firstFilm, actualFilm);

        verify(filmRepository, times(1)).update(firstFilm);
    }

    @DisplayName("Должен выкинуть исключение при обновлении")
    @Test
    public void shouldThrowAnExceptionWhenUpdatingAMovie() {
        given(filmRepository.update(firstFilm)).willReturn(Optional.empty());

        assertThrows(EntityNotFoundException.class,
                () -> filmService.updateFilm(firstFilm));

        verify(filmRepository, times(1)).update(firstFilm);
    }

    @DisplayName("Должен вернуть список фильмов")
    @Test
    public void shouldReturnAListOfMovies() {
        List<Film> filmList = List.of(firstFilm, secondFilm);

        given(filmRepository.findAll()).willReturn(filmList);

        assertEquals(filmList, filmService.getAllFilms());

        verify(filmRepository, times(1)).findAll();
    }

    @DisplayName("Должен вернуть список общих фильмов")
    @Test
    public void shouldReturnMutualTopFilms() {
        List<Film> firstFilmsList = List.of(firstFilm, secondFilm);
        List<Film> secondFilmsList = List.of(secondFilm);

        given(filmRepository.findTopFilmsByUserId(1)).willReturn(firstFilmsList);
        given(filmRepository.findTopFilmsByUserId(2)).willReturn(secondFilmsList);

        List<Film> currentList = filmService.getMutualTopFilms(1, 2);

        assertTrue(currentList.contains(secondFilm));
        assertEquals(1, currentList.size());

        verify(filmRepository, times(2)).findTopFilmsByUserId(any(Long.class));
    }
}