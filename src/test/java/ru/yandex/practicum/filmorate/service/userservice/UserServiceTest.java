package ru.yandex.practicum.filmorate.service.userservice;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.yandex.practicum.filmorate.dao.DAO;
import ru.yandex.practicum.filmorate.error.SaveUserException;
import ru.yandex.practicum.filmorate.error.UnknownUserException;
import ru.yandex.practicum.filmorate.model.User;

import java.time.LocalDate;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {

    @Mock
    private DAO<User> userDAO;

    @InjectMocks
    private UserServiceImpl userService;

    private User firstUser;
    private User secondUser;

    @BeforeEach
    void setUp() {
        firstUser = new User(1,
                "se.pa.94@mail.ru",
                "login",
                "name",
                LocalDate.of(1994, 3, 25),
                Collections.emptySet());
        secondUser = new User(2,
                "se.pa.95@mail.ru",
                "login",
                null,
                LocalDate.of(1995, 3, 25),
                Collections.emptySet());
    }


    @DisplayName("Должен сохранить и вернуть пользователя")
    @Test
    public void shouldSaveAndReturnTheMovie() {
        given(userDAO.save(firstUser)).willReturn(Optional.of(firstUser));

        User actualFilm = userService.createUser(firstUser);

        assertEquals(firstUser, actualFilm);

        verify(userDAO, times(1)).save(firstUser);
    }

    @DisplayName("Должен сохранить пользователя, заменив null name на login")
    @Test
    public void shouldSaveAndReturnTheMovieReplacingAnEmptyName() {
        given(userDAO.save(secondUser)).willReturn(Optional.of(secondUser));

        assertNull(secondUser.getName());

        User actualFilm = userService.createUser(secondUser);

        assertEquals(secondUser, actualFilm);
        assertEquals(secondUser.getLogin(), secondUser.getName());

        verify(userDAO, times(1)).save(secondUser);
    }

    @DisplayName("Должен выкинуть исключение при сохранении пользователя")
    @Test
    public void shouldThrowAnExceptionWhenSavingAMovie() {
        given(userDAO.save(firstUser)).willReturn(Optional.empty());

        assertThrows(SaveUserException.class,
                () -> userService.createUser(firstUser));

        verify(userDAO, times(1)).save(firstUser);
    }


    @DisplayName("Должен обновить и вернуть пользователя")
    @Test
    public void shouldUpdateAndReturnTheMovie() {
        given(userDAO.update(firstUser)).willReturn(Optional.of(firstUser));

        User actualUser = userService.updateUser(firstUser);

        assertEquals(firstUser, actualUser);

        verify(userDAO, times(1)).update(firstUser);
    }

    @DisplayName("Должен выкинуть исключение при обновлении пользователя")
    @Test
    public void shouldThrowAnExceptionWhenUpdatingAMovie() {
        given(userDAO.update(firstUser)).willReturn(Optional.empty());

        assertThrows(UnknownUserException.class,
                () -> userService.updateUser(firstUser));

        verify(userDAO, times(1)).update(firstUser);
    }

    @DisplayName("Должен вернуть список пользователей")
    @Test
    public void shouldReturnAListOfMovies() {
        List<User> userList = List.of(firstUser, secondUser);

        given(userDAO.getAll()).willReturn(userList);

        assertEquals(userList, userService.getAllUsers());

        verify(userDAO, times(1)).getAll();
    }
}