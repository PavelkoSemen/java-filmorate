package ru.yandex.practicum.filmorate.dao.jdbctemplateimpl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.dao.MpaRepository;
import ru.yandex.practicum.filmorate.model.Mpa;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

@Repository
@Slf4j
@RequiredArgsConstructor
public class JdbcMpaRepository implements MpaRepository {
    private final String getMpaById = "SELECT * FROM mpa WHERE mpa_id = ?";
    private final String getAllMpa = "SELECT * FROM mpa";
    private final JdbcTemplate jdbcTemplate;

    @Override
    public Optional<Mpa> findMpaById(long id) {
        log.info("Возрастное ограничение с id: {}", id);
        return jdbcTemplate.query(getMpaById, this::mapRow, id).stream().findAny();
    }

    @Override
    public List<Mpa> findAll() {
        log.info("Получение списка всех возрастных ограничений");
        return jdbcTemplate.query(getAllMpa, this::mapRow);
    }

    public Mpa mapRow(ResultSet rs, int rowNum) throws SQLException {
        Mpa mpa = new Mpa();
        mpa.setId(rs.getLong("MPA_ID"));
        mpa.setName(rs.getString("MPA_NAME"));
        mpa.setDescription(rs.getString("MPA_DESCRIPTION"));
        return mpa;
    }
}