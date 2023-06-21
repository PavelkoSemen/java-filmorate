package ru.yandex.practicum.filmorate.dao;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import ru.yandex.practicum.filmorate.model.User;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Optional;

@SpringBootTest
@AutoConfigureTestDatabase
@RequiredArgsConstructor(onConstructor_ = @Autowired)
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
class UserDeletingTests {
    private final int offset = 3;
    private final UserRepository userStorage;

    private void createUser() {
        var testUser = new User();
        testUser.setName("name");
        testUser.setLogin("login");
        var date = LocalDate.parse("20230101", DateTimeFormatter.BASIC_ISO_DATE);
        testUser.setBirthday(date);
        testUser.setEmail("aaa@bbb.ru");
        userStorage.save(testUser);
    }

    private void createUser1() {
        var testUser = new User();
        testUser.setName("name1");
        testUser.setLogin("login11");
        var date = LocalDate.parse("20230101", DateTimeFormatter.BASIC_ISO_DATE);
        testUser.setBirthday(date);
        testUser.setEmail("aaa@bbb1.ru");
        userStorage.save(testUser);
    }

    private void checkUser(Optional<User> userOpt) {
        var user = userOpt.get();
        var date = LocalDate.parse("20230101", DateTimeFormatter.BASIC_ISO_DATE);
        Assertions.assertEquals("name", user.getName());
        Assertions.assertEquals("login", user.getLogin());
        Assertions.assertEquals("aaa@bbb.ru", user.getEmail());
        Assertions.assertEquals(date, user.getBirthday());

    }

    private void checkUser1(Optional<User> userOpt) {
        var user = userOpt.get();
        var date = LocalDate.parse("20230101", DateTimeFormatter.BASIC_ISO_DATE);
        Assertions.assertEquals("name1", user.getName());
        Assertions.assertEquals("login11", user.getLogin());
        Assertions.assertEquals("aaa@bbb1.ru", user.getEmail());
        Assertions.assertEquals(date, user.getBirthday());
    }

    @Test
    void testFindUserById() {
        createUser();
        var user = userStorage.get(offset + 1);
        checkUser(user);
    }

    @Test
    void testFindUser1ById() {
        createUser1();
        var user = userStorage.get(offset + 1);
        checkUser1(user);
    }

    @Test
    void testGetUsers() {
        createUser();
        createUser1();
        var users = userStorage.getAll();
        Assertions.assertEquals(offset + 2, users.size());
        checkUser(users.stream().skip(offset).findFirst());
        checkUser1(users.stream().skip(offset + 1).findFirst());
    }

    @Test
    void testDeleteFilm() {
        createUser();
        var user = userStorage.get(1 + offset);
        userStorage.delete(user.orElse(null));
        var films = userStorage.getAll();
        Assertions.assertEquals(offset, films.size());
    }

    @Test
    void testAdd2UsersDelete1User() {
        createUser();
        createUser1();
        var user = userStorage.get(1 + offset);
        userStorage.delete(user.orElse(null));
        var films = userStorage.getAll();
        checkUser1(films.stream().skip(offset).findFirst());
    }

    @Test
    void testAddGetFriends() {
        createUser();
        createUser1();
        var user = userStorage.get(1 + offset);
        var user1 = userStorage.get(2 + offset);
        userStorage.insertFriend(user.get().getId(), user1.get().getId());
        var friends = userStorage.getFriendsList(user.get().getId());
        Assertions.assertEquals(1, friends.size());
        checkUser1(friends.stream().findFirst());
        userStorage.delete(user1.orElse(null));
        var friendsAfterDeleting = userStorage.getFriendsList(user.get().getId());
        Assertions.assertEquals(0, friendsAfterDeleting.size());
    }
}