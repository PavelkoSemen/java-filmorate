package ru.yandex.practicum.filmorate.dao.impl;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.context.SpringBootTest;
import ru.yandex.practicum.filmorate.model.User;

import javax.transaction.Transactional;
import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;
@SpringBootTest(
        webEnvironment = SpringBootTest.WebEnvironment.MOCK,
        classes = HibernateUserDAO.class)
class HibernateUserDAOTest {

    @Autowired
    HibernateUserDAO userDAO;

    @BeforeEach
    void setUp() {
    }

    @Test
    void save() {


        User firstUser = new User();
        firstUser.setEmail("se.pa.94@mail.ru");
        firstUser.setLogin("login");
        firstUser.setName("name");
        firstUser.setBirthday(LocalDate.of(1994, 3, 25));

        User secondUser = new User();
        secondUser.setEmail("se.pa.95@mail.ru");
        secondUser.setLogin("login");
        secondUser.setName("name");
        secondUser.setBirthday(LocalDate.of(1995, 3, 25));


        userDAO.save(firstUser);
        userDAO.save(secondUser);

        User friend = userDAO.get(1).orElse(null);
        User friend2 = userDAO.get(2).orElse(null);

        System.out.println(firstUser);
        System.out.println(secondUser);



        friend.addFriend(friend2);

        userDAO.save(friend);

        System.out.println(11);
    }
}