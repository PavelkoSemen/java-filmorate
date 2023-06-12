package ru.yandex.practicum.filmorate.dao.jdbctemplateimpl;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Primary;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.dao.UserRepository;
import ru.yandex.practicum.filmorate.model.User;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

import static ru.yandex.practicum.filmorate.utils.UsersSQL.*;


@Repository
@Primary
@Slf4j
public class JdbcUserRepository implements UserRepository {

    private final JdbcTemplate jdbcTemplate;

    @Autowired
    public JdbcUserRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public List<User> getAll() {
        log.info("Получение списка всех пользователей");
        return jdbcTemplate.query(GET_ALL_USERS, this::extractData);
    }

    @Override
    public Optional<User> get(long id) {
        log.info("Получение пользователя с id: {}", id);
        return jdbcTemplate.query(GET_USER_BY_ID, this::extractData, id).stream().findAny();
    }

    @Override
    public Optional<User> save(User user) {
        log.info("Сохранение пользователя: {}", user);
        SimpleJdbcInsert simpleJdbcInsert = new SimpleJdbcInsert(jdbcTemplate)
                .withTableName("users")
                .usingGeneratedKeyColumns("user_id");

        long id = simpleJdbcInsert.executeAndReturnKey(user.toMap()).longValue();
        user.setId(id);
        log.info("Пользователь {} сохранен", user.getId());
        return Optional.of(user);
    }

    @Override
    public Optional<User> update(User user) {
        log.info("Обновление пользователя: {}", user);
        int countRows = jdbcTemplate.update(UPDATE_USER,
                user.getEmail(),
                user.getLogin(),
                user.getName(),
                user.getBirthday(),
                user.getId());

        if (countRows > 0) {
            log.info("Пользователь {} обновлен", user);
            return Optional.of(user);
        }
        return Optional.empty();
    }

    @Override
    public void insertFriend(long userId, long friendId) {
        log.info("Добавление пользователю {} друга {}", userId, friendId);
        jdbcTemplate.update(INSERT_INTO_FRIENDS, userId, friendId);
        log.info("Друг {} добавлен пользователю {}", userId, friendId);
    }

    @Override
    public void deleteFriend(long userId, long friendId) {
        log.info("Удаление у пользователя {} друга {}", userId, friendId);
        jdbcTemplate.update(DELETE_FRIENDS, userId, friendId);
        log.info("Друг {} удален у пользователя {}", userId, friendId);
    }

    @Override
    public List<User> getMutualFriendsList(long id, long otherId) {
        log.info("Получение списка пересекающихся друзей у {},{}", id, otherId);
        return jdbcTemplate.query(GET_MUTUAL_FRIENDS, this::extractData, id, otherId);
    }

    @Override
    public List<User> getFriendsList(long id) {
        log.info("Получение списка друзей пользователя {}", id);
        return jdbcTemplate.query(GET_FRIENDS, this::extractData, id);
    }

    @Override
    public void delete(User user) {
        log.info("Удаление пользователя: {}", user);
        jdbcTemplate.update(DELETE_USER, user.getId());
        log.info("Пользователь {} удален", user);
    }

    private User extractData(ResultSet rs, int rowNum) throws SQLException {
        User user = new User();
        user.setId(rs.getLong("USER_ID"));
        user.setName(rs.getString("NAME"));
        user.setLogin(rs.getString("LOGIN"));
        user.setEmail(rs.getString("EMAIL"));
        user.setBirthday(rs.getDate("BIRTHDAY").toLocalDate());
        return user;
    }
}