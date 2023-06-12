package ru.yandex.practicum.filmorate.dao.jdbctemplateimpl;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Primary;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.dao.MpaRepository;
import ru.yandex.practicum.filmorate.model.Mpa;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

@Repository
@Primary
@Slf4j
public class JdbcMpaRepository implements MpaRepository {
    private final String GET_MPA_BY_ID = "SELECT * FROM mpa WHERE mpa_id = ?";
    private final String GET_ALL_MPA = "SELECT * FROM mpa";
    private final JdbcTemplate jdbcTemplate;

    @Autowired
    public JdbcMpaRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public Optional<Mpa> get(long id) {
        log.info("Возрастное ограничение с id: {}", id);
        return jdbcTemplate.query(GET_MPA_BY_ID, this::mapRow, id).stream().findAny();
    }

    @Override
    public List<Mpa> getAll() {
        log.info("Получение списка всех возрастных ограничений");
        return jdbcTemplate.query(GET_ALL_MPA, this::mapRow);
    }

    public Mpa mapRow(ResultSet rs, int rowNum) throws SQLException {
        Mpa mpa = new Mpa();
        mpa.setId(rs.getLong("MPA_ID"));
        mpa.setName(rs.getString("MPA_NAME"));
        mpa.setDescription(rs.getString("MPA_DESCRIPTION"));
        return mpa;
    }
}