package ru.yandex.practicum.filmorate.dao;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;
import ru.yandex.practicum.filmorate.model.User;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@AutoConfigureTestDatabase
@Transactional
@RequiredArgsConstructor(onConstructor_ = @Autowired)
class UserRepositoryTest {
    @Autowired
    private UserRepository userRepository;
    private User firstUser;

    @BeforeEach
    void setUp() {
        firstUser = new User();
        firstUser.setName("name4");
        firstUser.setLogin("login4");
        firstUser.setEmail("email4@mail.ru");
        firstUser.setBirthday(LocalDate.of(2003, 12, 1));
    }

    @Test
    @DisplayName("Должен вернуть список пользователей")
    void shouldReturnAllUsers() {
        List<User> userList = userRepository.getAll();
        assertThat(userList).hasSize(3);
    }

    @Test
    @DisplayName("Должен вернуть пустой Optional<User>")
    void shouldReturnEmptyOptional() {
        Optional<User> userOptional = userRepository.get(4);
        assertThat(userOptional).isEmpty();
    }

    @Test
    @DisplayName("Должен сохранить пользователя")
    void shouldSaveTheUser() {
        Optional<User> optionalUser = userRepository.save(firstUser);
        assertThat(optionalUser)
                .isPresent()
                .hasValueSatisfying(user ->
                        assertThat(user).hasFieldOrPropertyWithValue("id", 4L));

        System.out.println(userRepository.getAll());
    }

    @Test
    @DisplayName("Должен обновить пользователя")
    void shouldUpdateTheUser() {
        firstUser.setId(2);
        firstUser.setName("newName3");
        Optional<User> optionalUser = userRepository.update(firstUser);
        assertThat(optionalUser)
                .isPresent().contains(firstUser);
    }

    @Test
    @DisplayName("Должен добавить друга")
    void shouldAddAFriend() {
        List<User> oldUserList = userRepository.getFriendsList(1);
        assertThat(oldUserList).hasSize(0);

        userRepository.insertFriend(1, 2);

        List<User> newUserList = userRepository.getFriendsList(1);
        assertThat(newUserList)
                .hasSize(1)
                .element(0)
                .hasFieldOrPropertyWithValue("id", 2L);
    }

    @Test
    @DisplayName("Должен удалить друга")
    void shouldDeletedAFriend() {
        userRepository.insertFriend(1, 2);

        List<User> oldUserList = userRepository.getFriendsList(1);
        assertThat(oldUserList)
                .hasSize(1)
                .element(0)
                .hasFieldOrPropertyWithValue("id", 2L);

        userRepository.deleteFriend(1, 2);

        List<User> newUserList = userRepository.getFriendsList(1);
        assertThat(newUserList)
                .hasSize(0);
    }

    @Test
    @DisplayName("Должен вернуть общих друзей")
    void shouldReturnMutualFriendsList() {
        userRepository.insertFriend(1, 2);
        userRepository.insertFriend(3, 2);

        List<User> users = userRepository.getMutualFriendsList(1, 3);
        assertThat(users).hasSize(1)
                .element(0)
                .hasFieldOrPropertyWithValue("id", 2L);
    }

    @Test
    @DisplayName("Должен вернуть список друзей")
    void shouldReturnFriendsList() {
        userRepository.insertFriend(1, 2);

        List<User> oldUserList = userRepository.getFriendsList(1);
        assertThat(oldUserList)
                .hasSize(1)
                .element(0)
                .hasFieldOrPropertyWithValue("id", 2L);
    }
}