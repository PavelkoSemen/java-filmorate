package ru.yandex.practicum.filmorate;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;
import ru.yandex.practicum.filmorate.dao.DAO;
import ru.yandex.practicum.filmorate.dao.impl.HibernateUserDAO;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.service.userservice.UserService;

import java.time.LocalDate;
import java.util.Collections;
import java.util.List;


// Юнит тесты напишу ко второму заданию, не сильно их люблю.
@SpringBootApplication
public class FilmorateApplication {

    public static void main(String[] args) {
        ConfigurableApplicationContext context = SpringApplication.run(FilmorateApplication.class, args);

        UserService userService = context.getBean(UserService.class);
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


        userService.createUser(firstUser);
        userService.createUser(secondUser);

        userService.addFriend(firstUser.getId(), secondUser.getId());
//
//        List<User> friends = userService.getFriends(1);
//        System.out.println(1);


        //userService.removeFriend(firstUser.getId(), secondUser.getId());

    }
}