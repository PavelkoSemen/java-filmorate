package ru.yandex.practicum.filmorate.dao.jdbctemplateimpl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.dao.ReviewRepository;
import ru.yandex.practicum.filmorate.model.Review;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;


@Repository
@Slf4j
@RequiredArgsConstructor
public class JdbcReviewRepository implements ReviewRepository {

    private final JdbcTemplate jdbcTemplate;

    /*
        Вспомогательные методы класса репозитория
     */
    private Review mapRowReview(ResultSet rs, int rowNum) throws SQLException {

        Review review = new Review();
        review.setReviewId(rs.getLong("REVIEW_ID"));
        review.setContent(rs.getString("CONTENT"));
        review.setIsPositive(rs.getBoolean("IS_POSITIVE"));
        review.setUseful(rs.getInt("USEFUL"));
        review.setUserId(rs.getLong("USER_ID"));
        review.setFilmId(rs.getLong("FILM_ID"));

        log.info("Заполнение отзыва");

        return review;
    }

    private void actualizeUseful(Long reviewId) {

        String sql = "SELECT SUM(rate) FROM review_likes WHERE review_id = ?";

        Integer useful = jdbcTemplate.queryForObject(sql, Integer.class, reviewId);

        sql = "UPDATE reviews SET useful = ? WHERE review_id = ?";

        jdbcTemplate.update(sql, useful, reviewId);
    }

    private void deleteRating(Long reviewId, Long userId) {

        String sql = "DELETE FROM review_likes WHERE review_id = ? AND user_id = ?";

        jdbcTemplate.update(sql, reviewId, userId);
    }

    /*
        Методы имплементации интерфейса репозитория
     */
    @Override
    public boolean containsKey(Long reviewId) {

        SqlRowSet reviewRows = jdbcTemplate.queryForRowSet("SELECT * FROM reviews WHERE review_id = ?", reviewId);
        return reviewRows.next();
    }

    @Override
    public boolean containsFilm(Long filmId) {

        SqlRowSet filmRows = jdbcTemplate.queryForRowSet("SELECT * FROM films WHERE film_id = ?", filmId);
        return filmRows.next();
    }

    @Override
    public boolean containsUser(Long userId) {

        SqlRowSet userRows = jdbcTemplate.queryForRowSet("SELECT * FROM users WHERE user_id = ?", userId);
        return userRows.next();
    }

    @Override
    public List<Review> findAll(Long filmId, Integer count) {

        log.info("Получение списка всех отзывов");

        String sql;

        // по-умолчанию в контроллере параметр запроса filmId = -1 если не указан в запросе
        if (filmId >= 0) {
            sql = "SELECT * FROM reviews WHERE film_id = ? LIMIT ?";
            return jdbcTemplate.query(sql, this::mapRowReview, filmId, count);
        }

        sql = "SELECT * FROM reviews LIMIT ?";
        return jdbcTemplate.query(sql, this::mapRowReview, count);
    }

    @Override
    public Review findReviewById(Long reviewId) {

        log.info("Получение отзыва с id: {}", reviewId);

        String sql = "SELECT * FROM reviews WHERE review_id = ?";

        return jdbcTemplate.queryForObject(sql, this::mapRowReview, reviewId);

    }

    @Override
    public Review save(Review review) {

        log.info("Сохранение отзыва: {}", review);

        String content = review.getContent();
        boolean isPositive = review.getIsPositive();
        Integer useful = 0; // у нового фильма рейтинг 0
        Long userId = review.getUserId();
        Long filmId = review.getFilmId();

        // добавляем отзыв в таблицу reviews
        String sqlQuery = "INSERT INTO reviews(" +
                "content, " +
                "is_positive, " +
                "useful, " +
                "user_id, " +
                "film_id) " +
                "VALUES (?, ?, ?, ?, ?)";

        jdbcTemplate.update(sqlQuery,
                content,
                isPositive,
                useful,
                userId,
                filmId);

        // Получаем id добавленного отзыва в БД
        Long reviewId = jdbcTemplate.queryForObject("SELECT review_id FROM reviews WHERE " +
                        "content = '" + content + "' AND user_id = '" + userId + "' AND film_id = '" + filmId + "'",
                Long.class);

        review.setReviewId(reviewId);
        review.setUseful(useful);

        log.info("Отзыв {} сохранен", review);

        return review;
    }

    @Override
    public Review update(Review review) {

        log.info("Обновление отзыва: {}", review);

        Long reviewId = review.getReviewId();
        String content = review.getContent();
        boolean isPositive = review.getIsPositive();

        String sql = "UPDATE reviews SET " +
                "content = ?, " +
                "is_positive = ? " +
                "WHERE review_id = ?";

        jdbcTemplate.update(sql,
                content,
                isPositive,
                reviewId);

        // достаём обновлённый отзыв из таблицы отзывов
        sql = "SELECT * FROM reviews WHERE review_id = ?";

        return jdbcTemplate.queryForObject(sql, this::mapRowReview, reviewId);
    }

    @Override
    public void delete(Long reviewId) {

        log.info("Удаление отзыва: {}", reviewId);

        String sql = "DELETE FROM reviews WHERE review_id = ?";

        jdbcTemplate.update(sql, reviewId);
    }

    @Override
    public void putLike(Long reviewId, Long userId) {

        log.info("Добавить отзыву {} лайк от пользователя {}", reviewId, userId);

        deleteRating(reviewId, userId);

        int rate = 1;

        String sql = "INSERT INTO review_likes(review_id, user_id, rate) VALUES (?, ?, ?)";

        jdbcTemplate.update(sql, reviewId, userId, rate);

        actualizeUseful(reviewId);

        log.info("Добавлен лайк отзыву {} от пользователя {}", reviewId, userId);
    }

    @Override
    public void deleteLike(Long reviewId, Long userId) {

        log.info("Удалить у отзыва {} лайк, от пользователя {}", reviewId, userId);

        deleteRating(reviewId, userId);

        actualizeUseful(reviewId);

        log.info("Удален лайк у отзыва {}, от пользователя {}", reviewId, userId);
    }

    @Override
    public void putDislike(Long reviewId, Long userId) {

        log.info("Добавить отзыву {} дизлайк от пользователя {}", reviewId, userId);

        deleteRating(reviewId, userId);

        int rate = -1;

        String sql = "INSERT INTO review_likes(review_id, user_id, rate) VALUES (?, ?, ?)";

        jdbcTemplate.update(sql, reviewId, userId, rate);

        actualizeUseful(reviewId);

        log.info("Добавлен дизлайк отзыву {} от пользователя {}", reviewId, userId);
    }

    @Override
    public void deleteDislike(Long reviewId, Long userId) {

        log.info("Удалить у отзыва {} дизлайк от пользователя {}", reviewId, userId);

        deleteRating(reviewId, userId);

        actualizeUseful(reviewId);

        log.info("Удален дизлайк у отзыва {} от пользователя {}", reviewId, userId);
    }
}