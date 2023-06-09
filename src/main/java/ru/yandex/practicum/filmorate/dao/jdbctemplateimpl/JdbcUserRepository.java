package ru.yandex.practicum.filmorate.dao.jdbctemplateimpl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.dao.UserRepository;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.User;

import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Collection;
import java.util.List;
import java.util.Optional;

import static ru.yandex.practicum.filmorate.utils.sqlscript.UsersSQL.*;


@Repository
@Slf4j
@RequiredArgsConstructor
public class JdbcUserRepository implements UserRepository {

    private final JdbcTemplate jdbcTemplate;

    @Override
    public List<User> findAll() {
        log.info("Получение списка всех пользователей");
        return jdbcTemplate.query(getAllUsers, this::extractData);
    }

    @Override
    public Optional<User> findUserById(long id) {
        log.info("Получение пользователя с id: {}", id);
        return jdbcTemplate.query(getUserById, this::extractData, id).stream().findAny();
    }

    @Override
    public Optional<User> save(User user) {
        log.info("Сохранение пользователя: {}", user);
        KeyHolder keyHolder = new GeneratedKeyHolder();
        jdbcTemplate.update(connection -> {
            PreparedStatement stmt = connection.prepareStatement(insertIntoUser, new String[]{"USER_ID"});
            stmt.setString(1, user.getEmail());
            stmt.setString(2, user.getLogin());
            stmt.setString(3, user.getName());
            stmt.setDate(4, Date.valueOf(user.getBirthday()));
            return stmt;
        }, keyHolder);

        user.setId(keyHolder.getKey().longValue());
        log.info("Пользователь {} сохранен", user.getId());
        return Optional.of(user);
    }

    @Override
    public Optional<User> update(User user) {
        log.info("Обновление пользователя: {}", user);
        int countRows = jdbcTemplate.update(updateUser,
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
    public boolean insertFriend(long userId, long friendId) {
        log.info("Добавление пользователю {} друга {}", userId, friendId);
        int countRows = jdbcTemplate.update(deleteFriends, userId, friendId);
        jdbcTemplate.update(insertIntoFriends, userId, friendId);
        return countRows == 0;
    }

    @Override
    public boolean deleteFriend(long userId, long friendId) {
        log.info("Удаление у пользователя {} друга {}", userId, friendId);
        return jdbcTemplate.update(deleteFriends, userId, friendId) > 0;
    }

    @Override
    public List<User> findMutualFriendsList(long id, long otherId) {
        log.info("Получение списка пересекающихся друзей у {},{}", id, otherId);
        return jdbcTemplate.query(getMutualFriends, this::extractData, id, otherId);
    }

    @Override
    public List<User> findFriendsList(long id) {
        log.info("Получение списка друзей пользователя {}", id);
        return jdbcTemplate.query(getFriends, this::extractData, id);
    }

    @Override
    public void delete(User user) {
        log.info("Удаление пользователя: {}", user);
        jdbcTemplate.update(deleteUser, user.getId());
        log.info("Пользователь {} удален", user);
    }

    @Override
    public Collection<Film> findRecommendationFilms(long id) {
        return jdbcTemplate.query(queryFilmsRecommendations, JdbcFilmRepository::extractFilmData, id, id);
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