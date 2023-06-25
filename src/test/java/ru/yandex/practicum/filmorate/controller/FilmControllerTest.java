package ru.yandex.practicum.filmorate.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import ru.yandex.practicum.filmorate.error.EntityNotFoundException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Mpa;
import ru.yandex.practicum.filmorate.service.filmservice.FilmService;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Stream;

import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.hasSize;
import static org.junit.jupiter.params.provider.Arguments.arguments;
import static org.mockito.BDDMockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;


@SpringBootTest
@AutoConfigureMockMvc
class FilmControllerTest {

    @Autowired
    private MockMvc mvc;

    @MockBean
    private FilmService service;

    @Autowired
    private ObjectMapper objectMapper;

    private Film firstFilm;
    private Film secondFilm;

    @BeforeEach
    void setUp() {
        Mpa mpaFirst = new Mpa();
        mpaFirst.setId(1);
        Mpa mpaSecond = new Mpa();
        mpaSecond.setId(2);
        firstFilm = new Film(1,
                "nisi eiusmod",
                "adipisicing",
                LocalDate.of(1967, 3, 25),
                100,
                mpaFirst);
        secondFilm = new Film(2,
                "nisi eiusmod",
                "adipisicing",
                LocalDate.of(1967, 3, 25),
                100,
                mpaSecond);
    }

    @DisplayName("Должен вернуть код 4xx, и сообщение об ошибке")
    @ParameterizedTest
    @MethodSource("filmsAndErrorMessage")
    public void shouldThrowAnExceptionWhenTryingToAddAFilm(Film film, String errorMessage) throws Exception {
        mvc.perform(post("/films")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(film)))
                .andExpect(status().is4xxClientError())
                .andExpect(content().string(containsString(errorMessage)))
                .andDo(print());
    }

    @DisplayName("Должен создать фильм и вернуть его обратно")
    @Test
    public void shouldCreateAMovieAndBringItBack() throws Exception {

        given(service.createFilm(firstFilm)).willReturn(firstFilm);

        mvc.perform(post("/films")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(firstFilm)))
                .andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(firstFilm)))
                .andDo(print());

        verify(service, times(1)).createFilm(firstFilm);
    }

    @Test
    @DisplayName("Должен обновить и вернуть фильм")
    public void shouldUpdateAndReturnFilm() throws Exception {

        given(service.updateFilm(firstFilm)).willReturn(firstFilm);

        mvc.perform(put("/films").contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(firstFilm)))
                .andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(firstFilm)))
                .andDo(print());

        verify(service, times(1)).updateFilm(firstFilm);
    }

    @Test
    @DisplayName("Должен вернуть статус 404, при попытки обновить не существующий фильм")
    public void shouldReturnNotFoundUpdateFilm() throws Exception {

        given(service.updateFilm(firstFilm)).willThrow(new EntityNotFoundException());

        mvc.perform(put("/films").contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(firstFilm)))
                .andExpect(status().isNotFound())
                .andDo(print());

        verify(service, times(1)).updateFilm(firstFilm);
    }

    @Test
    @DisplayName("Должен вернуть список фильмов")
    public void givenFilmsWhenGetFilmsThenReturnJsonArray()
            throws Exception {


        List<Film> allFilms = Arrays.asList(firstFilm, secondFilm);

        given(service.getAllFilms()).willReturn(allFilms);

        mvc.perform(get("/films")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(2)))
                .andDo(print());

        verify(service, times(1)).getAllFilms();
    }

    static Stream<Arguments> filmsAndErrorMessage() {
        Mpa mpaFirst = new Mpa();
        mpaFirst.setId(1);
        Mpa mpaSecond = new Mpa();
        mpaSecond.setId(2);
        return Stream.of(
                arguments(
                        new Film(0, null, "Description",
                                LocalDate.of(1900, 3, 25), 200, mpaFirst),
                        "Name cannot be empty"),

                arguments(
                        new Film(0, "Name", "t".repeat(250),
                                LocalDate.of(1900, 3, 25), 200, mpaFirst),
                        "Description length is more than 200 characters"),

                arguments(
                        new Film(0, "Name", "Description",
                                LocalDate.of(1890, 3, 25), 200, mpaSecond),
                        "Invalid film realise date"),

                arguments(
                        new Film(0, "Name", "Description",
                                LocalDate.of(1980, 3, 25), -200, mpaSecond),
                        "Duration of the film is negative")
        );
    }
}