package ru.yandex.practicum.filmorate.dao.jdbctemplateimpl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.dao.GenreRepository;
import ru.yandex.practicum.filmorate.model.Genre;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

@Repository
@Slf4j
@RequiredArgsConstructor
public class JdbcGenreRepository implements GenreRepository {

    private final String getGenreById = "SELECT * FROM genres WHERE genre_id = ?";
    private final String getAllGenres = "SELECT * FROM genres";

    private final JdbcTemplate jdbcTemplate;

    @Override
    public Optional<Genre> findGenreById(long id) {
        log.info("Получение жанра с id: {}", id);
        return jdbcTemplate.query(getGenreById, this::mapRow, id).stream().findAny();
    }

    @Override
    public List<Genre> findAll() {
        log.info("Получение списка всех жанров");
        return jdbcTemplate.query(getAllGenres, this::mapRow);
    }


    public Genre mapRow(ResultSet rs, int rowNum) throws SQLException {
        Genre genre = new Genre();
        genre.setId(rs.getLong("GENRE_ID"));
        genre.setName(rs.getString("GENRE_NAME"));
        return genre;
    }
}