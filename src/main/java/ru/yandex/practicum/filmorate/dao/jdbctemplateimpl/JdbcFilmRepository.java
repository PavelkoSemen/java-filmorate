package ru.yandex.practicum.filmorate.dao.jdbctemplateimpl;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Primary;
import org.springframework.jdbc.core.BatchPreparedStatementSetter;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.dao.FilmRepository;
import ru.yandex.practicum.filmorate.error.UnknownFilmException;
import ru.yandex.practicum.filmorate.model.Director;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.model.Mpa;

import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;
import java.util.stream.Collectors;

import static ru.yandex.practicum.filmorate.utils.FilmsSQL.*;

@Repository
@Primary
@Slf4j
public class JdbcFilmRepository implements FilmRepository {

    private final JdbcTemplate jdbcTemplate;

    @Autowired
    public JdbcFilmRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public List<Film> getAll() {
        log.info("Получение списка всех фильмов");
        return jdbcTemplate.query(getAllFilms, this::extractData);
    }

    @Override
    public Optional<Film> get(long id) {
        log.info("Получение фильма с id: {}", id);
        return jdbcTemplate.query(getFilmById, this::extractData, id).stream().findAny();
    }

    @Override
    public Optional<Film> save(Film film) {
        log.info("Сохранение фильма: {}", film);
        KeyHolder keyHolder = new GeneratedKeyHolder();
        jdbcTemplate.update(connection -> {
            PreparedStatement stmt = connection.prepareStatement(insertIntoFilm, new String[]{"FILM_ID"});
            stmt.setString(1, film.getName());
            stmt.setString(2, film.getDescription());
            stmt.setDate(3, Date.valueOf(film.getReleaseDate()));
            stmt.setInt(4, film.getDuration());
            stmt.setLong(5, film.getMpa().getId());
            return stmt;
        }, keyHolder);

        film.setId(keyHolder.getKey().longValue());
        insertGenres(film);
        insertDirectors(film);
        log.info("Фильма {} сохранен", film);
        return Optional.of(film);
    }

    @Override
    public Optional<Film> update(Film film) {
        log.info("Обновление фильма: {}", film);
        int countRows = jdbcTemplate.update(updateFilm,
                film.getName(),
                film.getDescription(),
                film.getReleaseDate(),
                film.getDuration(),
                film.getMpa().getId(),
                film.getId());

        jdbcTemplate.update(deleteFilmGenre, film.getId());
        jdbcTemplate.update(deleteFilmDirector, film.getId());

        insertGenres(film);
        insertDirectors(film);

        if (countRows > 0) {
            log.info("Фильма {} обновлен", film);
            return Optional.of(film);
        }
        return Optional.empty();
    }

    @Override
    public void putLike(long filmId, long userId) {
        log.info("Добавить фильму {} лайк, от пользователя {}", filmId, userId);
        jdbcTemplate.update(insertIntoLikes, filmId, userId);
        log.info("Добавлен лайк фильму {} , от пользователя {}", filmId, userId);
    }

    @Override
    public void deleteLike(long filmId, long userId) {
        log.info("Удалить у фильма {} лайк, от пользователя {}", filmId, userId);
        jdbcTemplate.update(deleteLikes, filmId, userId);
        log.info("Удален лайк у фильма {} , от пользователя {}", filmId, userId);
    }

    @Override
    public List<Film> findTopFilmsWithLimit(int countFilms) {
        log.info("Вернуть топ {} фильмов", countFilms);
        return jdbcTemplate.query(getTopFilmsWithLimit, this::extractData, countFilms);
    }

    @Override
    public List<Film> findTopFilms() {
        log.info("Вернуть топ фильмов");
        return jdbcTemplate.query(getTopFilms, this::extractData);
    }

    @Override
    public List<Film> findTopFilmsByUserId(long userId) {
        log.info("Вернуть топ фильмов для пользователя {}", userId);
        return jdbcTemplate.query(getTopFilmsByUserId, this::extractData, userId);
    }

    public List<Film> getFilmsByDirector(long directorId, String sortBy) {
        log.info("Вернуть фильмы режиссера с id {}. Дополнительное условие {}", directorId, sortBy);
        List<Film> list;
        switch (sortBy) {
            case("likes"):
                list = new LinkedList<>(Objects.requireNonNull(jdbcTemplate
                    .query(queryGetFilmsByDirectorLikeSort, this::extractData, directorId)));
                break;
            case("year"):
                list = new LinkedList<>(Objects.requireNonNull(jdbcTemplate
                    .query(queryGetFilmsByDirectorYearSort, this::extractData, directorId)));
                break;
            default: list = new LinkedList<>(Objects.requireNonNull(jdbcTemplate
                    .query(queryGetFilmsByDirectorWithoutSort, this::extractData, directorId)));
        }
        if (list.size() == 0) {
            throw new UnknownFilmException("У режиссера с id: " + directorId + " нет фильмов");
        }
        return list;
    }

    @Override
    public void delete(Film film) {
        log.info("Удаление фильма: {}", film);
        jdbcTemplate.update(deleteFilm, film.getId());
        log.info("Фильма {} удален", film);
    }

    private void insertGenres(Film film) {
        List<Long> genresId = film.getGenres().stream()
                .map(Genre::getId)
                .collect(Collectors.toList());
        if (!genresId.isEmpty()) {
            jdbcTemplate.batchUpdate(insertIntoFilmGenre, new BatchPreparedStatementSetter() {
                @Override
                public void setValues(PreparedStatement ps, int i) throws SQLException {
                    ps.setLong(1, film.getId());
                    ps.setLong(2, genresId.get(i));
                }

                @Override
                public int getBatchSize() {
                    return genresId.size();
                }
            });
        }
    }

    private void insertDirectors(Film film) {
        List<Long> directorsId = film.getDirectors().stream()
                .map(Director::getId)
                .collect(Collectors.toList());
        if (!directorsId.isEmpty()) {
            jdbcTemplate.batchUpdate(insertIntoFilmDirector, new BatchPreparedStatementSetter() {
                @Override
                public void setValues(PreparedStatement ps, int i) throws SQLException {
                    ps.setLong(1, film.getId());
                    ps.setLong(2, directorsId.get(i));
                }

                @Override
                public int getBatchSize() {
                    return directorsId.size();
                }
            });
        }
    }
    private List<Film> extractData(ResultSet rs) throws SQLException {
        return extractFilmData(rs);
    }
    public static List<Film> extractFilmData(ResultSet rs) throws SQLException {
        List<Film> list = new ArrayList<>();
        Film currentFilm = new Film();
        long previousId = 0;
        while (rs.next()) {
            if (previousId != rs.getLong("FILM_ID")) {
                currentFilm = mapRowFilms(rs);
                list.add(currentFilm);
                previousId = rs.getLong("FILM_ID");
            }
            if (rs.getLong("GENRE_ID") != 0) {
                currentFilm.addGenre(mapRowGenre(rs));
            }
            if (rs.getLong("DIRECTOR_ID") != 0) {
                currentFilm.addDirector(mapRowDirector(rs));
            }
        }
        return list;
    }

    private static Genre mapRowGenre(ResultSet rs) throws SQLException {
        log.info("Заполнение жанров");
        Genre genre = new Genre();
        genre.setId(rs.getLong("GENRE_ID"));
        genre.setName(rs.getString("GENRE_NAME"));
        return genre;
    }

    private static Director mapRowDirector(ResultSet rs) throws SQLException {
        log.info("Заполнение режиссеров");
        Director director = new Director();
        director.setId(rs.getLong("DIRECTOR_ID"));
        director.setName(rs.getString("DIRECTOR_NAME"));
        return director;
    }

    private static Film mapRowFilms(ResultSet rs) throws SQLException {
        log.info("Заполнение фильма");
        Film film = new Film();
        film.setId(rs.getLong("FILM_ID"));
        film.setName(rs.getString("NAME"));
        film.setDescription(rs.getString("DESCRIPTION"));
        film.setDuration(rs.getInt("DURATION"));
        film.setReleaseDate(rs.getDate("RELEASE").toLocalDate());
        Mpa mpa = new Mpa();
        mpa.setId(rs.getLong("MPA_ID"));
        mpa.setName(rs.getString("MPA_NAME"));
        mpa.setDescription(rs.getString("MPA_DESCRIPTION"));
        film.setMpa(mpa);
        return film;
    }

}