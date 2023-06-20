package ru.yandex.practicum.filmorate.dao.jdbctemplateimpl;

import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Primary;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.dao.DirectorRepository;
import ru.yandex.practicum.filmorate.model.Director;

import javax.swing.text.html.Option;
import java.sql.PreparedStatement;
import java.util.Optional;

@Repository
@Primary
@Slf4j
public class JdbcDirectorRepository implements DirectorRepository {
    private static final String queryAddFilmDirector = "INSERT INTO film_director(film_id, director_id) VALUES (?, ?)";
    private static final String queryAddDirector = "INSERT INTO directors(director_name) VALUES (?)";

    private final JdbcTemplate jdbcTemplate;

    public JdbcDirectorRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public Optional<Director> addFilmDirector(long filmId, Director director) {
        log.info("Добавление фильму с id {} режиссера {}", filmId, director.getName());
        try {
            KeyHolder keyHolder = new GeneratedKeyHolder();
            jdbcTemplate.update(connection -> {
                PreparedStatement ps = connection.prepareStatement(queryAddDirector, new String[]{"director_id"});
                ps.setString(1, director.getName());
                return ps;}, keyHolder);
            director.setId(keyHolder.getKey().longValue());
        } catch (DataAccessException ignore) {}
        jdbcTemplate.update (queryAddFilmDirector, filmId, director.getId());
        return Optional.of(director);
    }
}
