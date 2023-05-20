package ru.yandex.practicum.filmorate.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import ru.yandex.practicum.filmorate.error.UnknownUserException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.service.userservice.UserService;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Stream;

import static org.hamcrest.Matchers.*;
import static org.junit.jupiter.params.provider.Arguments.arguments;
import static org.mockito.BDDMockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(UserController.class)
class UserControllerTest {

    @Autowired
    private MockMvc mvc;

    @MockBean
    private UserService service;

    @Autowired
    private ObjectMapper objectMapper;

    private User firstUser;
    private User secondUser;


    @BeforeEach
    void setUp() {
        firstUser = new User(1,
                "se.pa.94@mail.ru",
                "login",
                "name",
                LocalDate.of(1994, 3, 25));
        secondUser = new User(2,
                "se.pa.95@mail.ru",
                "login",
                "name",
                LocalDate.of(1995, 3, 25));
    }

    @DisplayName("Должен вернуть код 4xx, и сообщение об ошибке")
    @ParameterizedTest
    @MethodSource("userAndErrorMessage")
    public void shouldThrowAnExceptionWhenTryingToAddAUser(User user, String errorMessage) throws Exception {
        mvc.perform(post("/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(user)))
                .andExpect(status().is4xxClientError())
                .andExpect(content().string(containsString(errorMessage)))
                .andDo(print());
    }

    @DisplayName("Должен создать пользователя и вернуть его обратно")
    @Test
    public void shouldCreateAUserAndBringItBack() throws Exception {
        given(service.createUser(firstUser)).willReturn(firstUser);

        mvc.perform(post("/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(firstUser)))
                .andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(firstUser)))
                .andDo(print());

        verify(service, times(1)).createUser(firstUser);
    }

    @Test
    @DisplayName("Должен обновить и вернуть пользователя")
    public void shouldUpdateAndReturnUser() throws Exception {

        given(service.updateUser(firstUser)).willReturn(firstUser);

        mvc.perform(put("/users").contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(firstUser)))
                .andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(firstUser)))
                .andDo(print());

        verify(service, times(1)).updateUser(firstUser);
    }

    @Test
    @DisplayName("Должен вернуть статус 404, при попытки обновить не существующего пользователя")
    public void shouldReturnNotFoundUpdateUser() throws Exception {

        given(service.updateUser(firstUser)).willThrow(new UnknownUserException());

        mvc.perform(put("/users").contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(firstUser)))
                .andExpect(status().isNotFound())
                .andDo(print());

        verify(service, times(1)).updateUser(firstUser);
    }

    @Test
    @DisplayName("Должен вернуть список пользователей")
    public void givenUsersWhenGetFilmsThenReturnJsonArray()
            throws Exception {


        List<User> allUsers = Arrays.asList(firstUser, secondUser);

        given(service.getAllUsers()).willReturn(allUsers);

        mvc.perform(get("/users")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[0].id").value(firstUser.getId()))
                .andExpect(jsonPath("$[1].id").value(secondUser.getId()))
                .andDo(print());
        verify(service, times(1)).getAllUsers();
    }


    static Stream<Arguments> userAndErrorMessage() {

        return Stream.of(
                arguments(
                        new User(0, "se.pa.94mail.ru", "login", "name",
                                LocalDate.of(1900, 3, 25)),
                        "Does not match the email"),

                arguments(
                        new User(0, "se.pa.94@mail.ru", null, "name",
                                LocalDate.of(1900, 3, 25)),
                        "Login cannot be empty"),

                arguments(
                        new User(0, "se.pa.94@mail.ru", "login", "name",
                                LocalDate.of(2900, 3, 25)),
                        "Birthday is longer than the current date")
        );
    }
}