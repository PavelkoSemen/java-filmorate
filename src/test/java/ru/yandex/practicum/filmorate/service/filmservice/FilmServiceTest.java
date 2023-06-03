package ru.yandex.practicum.filmorate.service.filmservice;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.yandex.practicum.filmorate.dao.FilmRepository;
import ru.yandex.practicum.filmorate.error.SaveFilmException;
import ru.yandex.practicum.filmorate.error.UnknownFilmException;
import ru.yandex.practicum.filmorate.model.Film;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
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
        firstFilm = new Film(1,
                "film1",
                "film1",
                LocalDate.of(2005, 3, 25),
                100);
        secondFilm = new Film(2,
                "film2",
                "film2",
                LocalDate.of(2015, 3, 25),
                100);
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

        assertThrows(SaveFilmException.class,
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

        assertThrows(UnknownFilmException.class,
                () -> filmService.updateFilm(firstFilm));

        verify(filmRepository, times(1)).update(firstFilm);
    }

    @DisplayName("Должен вернуть список фильмов")
    @Test
    public void shouldReturnAListOfMovies() {
        List<Film> filmList = List.of(firstFilm, secondFilm);

        given(filmRepository.getAll()).willReturn(filmList);

        assertEquals(filmList, filmService.getAllFilms());

        verify(filmRepository, times(1)).getAll();
    }
}