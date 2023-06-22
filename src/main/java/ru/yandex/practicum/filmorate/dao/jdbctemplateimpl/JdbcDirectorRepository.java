package ru.yandex.practicum.filmorate.dao.jdbctemplateimpl;

import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Primary;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.dao.DirectorRepository;
import ru.yandex.practicum.filmorate.model.Director;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Repository
@Primary
@Slf4j
public class JdbcDirectorRepository implements DirectorRepository {
    private final JdbcTemplate jdbcTemplate;
    public static final String queryGetDirector = "SELECT * FROM directors WHERE director_id = ?";
    public static final String queryCreateDirector = "INSERT INTO directors (director_name) VALUES (?)";
    public static final String queryUpdateDirector = "UPDATE directors SET director_name = ? WHERE director_id = ?";
    public static final String queryDeleteDirector = "DELETE FROM directors WHERE director_id = ?";
    public static final String queryGetAllDirectors = "SELECT * FROM directors";

    public JdbcDirectorRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public Optional<Director> getDirector(long id) {
        log.info("Получение режиссера с id: {}", id);
        return jdbcTemplate.query(queryGetDirector, this::extractData, id).stream().findAny();
    }

    public Optional<Director> createDirector(Director director) {
        log.info("Создание режиссера: {}", director);
        KeyHolder keyHolder = new GeneratedKeyHolder();
        jdbcTemplate.update(connection -> {
            PreparedStatement stmt = connection.prepareStatement(queryCreateDirector, new String[]{"DIRECTOR_ID"});
            stmt.setString(1, director.getName());
            return stmt;
        }, keyHolder);

        director.setId(keyHolder.getKey().longValue());
        log.info("Режиссер {} сохранен", director);
        return Optional.of(director);
    }

    public Optional<Director> updateDirector(Director director) {
        log.info("Обновление режиссера с id: {}", director.getId());
        int countRows = jdbcTemplate.update(queryUpdateDirector,
                director.getName(),
                director.getId());
        if (countRows > 0) {
            log.info("Режиссер {} обновлен", director);
            return Optional.of(director);
        }
        return Optional.empty();
    }

    public void deleteDirector(long id) {
        log.info("Удаление режиссера с id: {}", id);
        jdbcTemplate.update(queryDeleteDirector, id);
    }

    public List<Director> getAllDirectors() {
        log.info("Получение всех режиссеров");
        return jdbcTemplate.query(queryGetAllDirectors, this::extractData);
    }

    private List<Director> extractData(ResultSet rs) throws SQLException {
        List<Director> list = new ArrayList<>();
        long previousId = 0;
        while (rs.next()) {
            if (previousId != rs.getLong("DIRECTOR_ID")) {
                list.add(mapRowDirector(rs));
                previousId = rs.getLong("DIRECTOR_ID");
            }
        }
        return list;
    }

    private Director mapRowDirector(ResultSet rs) throws SQLException {
        log.info("Заполнение режиссеров");
        Director director = new Director();
        director.setId(rs.getLong("DIRECTOR_ID"));
        director.setName(rs.getString("DIRECTOR_NAME"));
        return director;
    }
}
