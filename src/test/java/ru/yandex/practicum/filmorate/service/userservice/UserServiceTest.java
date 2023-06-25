package ru.yandex.practicum.filmorate.service.userservice;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.yandex.practicum.filmorate.dao.UserRepository;
import ru.yandex.practicum.filmorate.error.EntityNotFoundException;
import ru.yandex.practicum.filmorate.error.EntitySaveException;
import ru.yandex.practicum.filmorate.model.User;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {

    @Mock
    private UserRepository userRepository;

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
                LocalDate.of(1994, 3, 25));
        secondUser = new User(2,
                "se.pa.95@mail.ru",
                "login",
                null,
                LocalDate.of(1995, 3, 25));
    }


    @DisplayName("Должен сохранить и вернуть пользователя")
    @Test
    public void shouldSaveAndReturnTheMovie() {
        given(userRepository.save(firstUser)).willReturn(Optional.of(firstUser));

        User actualFilm = userService.createUser(firstUser);

        assertEquals(firstUser, actualFilm);

        verify(userRepository, times(1)).save(firstUser);
    }

    @DisplayName("Должен сохранить пользователя, заменив null name на login")
    @Test
    public void shouldSaveAndReturnTheMovieReplacingAnEmptyName() {
        given(userRepository.save(secondUser)).willReturn(Optional.of(secondUser));

        assertNull(secondUser.getName());

        User actualFilm = userService.createUser(secondUser);

        assertEquals(secondUser, actualFilm);
        assertEquals(secondUser.getLogin(), secondUser.getName());

        verify(userRepository, times(1)).save(secondUser);
    }

    @DisplayName("Должен выкинуть исключение при сохранении пользователя")
    @Test
    public void shouldThrowAnExceptionWhenSavingAMovie() {
        given(userRepository.save(firstUser)).willReturn(Optional.empty());

        assertThrows(EntitySaveException.class,
                () -> userService.createUser(firstUser));

        verify(userRepository, times(1)).save(firstUser);
    }


    @DisplayName("Должен обновить и вернуть пользователя")
    @Test
    public void shouldUpdateAndReturnTheMovie() {
        given(userRepository.update(firstUser)).willReturn(Optional.of(firstUser));

        User actualUser = userService.updateUser(firstUser);

        assertEquals(firstUser, actualUser);

        verify(userRepository, times(1)).update(firstUser);
    }

    @DisplayName("Должен выкинуть исключение при обновлении пользователя")
    @Test
    public void shouldThrowAnExceptionWhenUpdatingAMovie() {
        given(userRepository.update(firstUser)).willReturn(Optional.empty());

        assertThrows(EntityNotFoundException.class,
                () -> userService.updateUser(firstUser));

        verify(userRepository, times(1)).update(firstUser);
    }

    @DisplayName("Должен вернуть список пользователей")
    @Test
    public void shouldReturnAListOfMovies() {
        List<User> userList = List.of(firstUser, secondUser);

        given(userRepository.findAll()).willReturn(userList);

        assertEquals(userList, userService.getAllUsers());

        verify(userRepository, times(1)).findAll();
    }
}