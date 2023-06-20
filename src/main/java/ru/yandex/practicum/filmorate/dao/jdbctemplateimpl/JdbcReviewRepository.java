package ru.yandex.practicum.filmorate.dao.jdbctemplateimpl;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Primary;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.dao.ReviewRepository;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Review;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static ru.yandex.practicum.filmorate.service.filmservice.utils.FilmsSQL.*;

@Repository
@Primary
@Slf4j
public class JdbcReviewRepository implements ReviewRepository {

    private final JdbcTemplate jdbcTemplate;

    @Autowired
    public JdbcReviewRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    private Review mapRowReview(ResultSet rs) throws SQLException {

        log.info("Заполнение отзыва");
        Review review = new Review();
        review.setReviewId(rs.getLong("REVIEW_ID"));
        review.setContent(rs.getString("CONTENT"));
        review.setPositive(rs.getBoolean("IS_POSITIVE"));
        review.setUserId(rs.getLong("USER_ID"));
        review.setFilmId(rs.getLong("FILM_ID"));
        review.setUseful(rs.getInt("USEFUL"));

        return review;
    }

    private List<Review> extractData(ResultSet rs) throws SQLException {

        List<Review> list = new ArrayList<>();
        Review currentReview;
        long previousId = 0;
        while (rs.next()) {
            if (previousId != rs.getLong("REVIEW_ID")) {
                currentReview = mapRowReview(rs);
                list.add(currentReview);
                previousId = rs.getLong("REVIEW_ID");
            }
        }

        return list;
    }

    /*
    доделать
    */
    @Override
    public List<Review> getAll() {

        log.info("Получение списка всех отзывов");
        return jdbcTemplate.query(getAllFilms, this::extractData);

    }

    /*
    доделать
     */
    @Override
    public Optional<Review> get(long reviewId) {

        log.info("Получение отзыва с id: {}", reviewId);
        return jdbcTemplate.query(getFilmById, this::extractData, reviewId).stream().findAny();

    }

    @Override
    public Optional<Review> save(Review review) {

        log.info("Сохранение отзыва: {}", review);
        KeyHolder keyHolder = new GeneratedKeyHolder();
        jdbcTemplate.update(connection -> {

            String sql = "INSERT INTO reviews(content, is_positive, useful, user_id, friend_id) VALUES (?, ?, ?, ?, ?)";

            int usefulIsZero = 0; // у нового отзыва полезность 0

            PreparedStatement stmt = connection.prepareStatement(sql, new String[]{"REVIEW_ID"});
            stmt.setString(1, review.getContent());
            stmt.setBoolean(2, review.isPositive());
            stmt.setInt(3, usefulIsZero);
            stmt.setLong(4, review.getUserId());
            stmt.setLong(5, review.getFilmId());
            return stmt;
        }, keyHolder);

        review.setReviewId(keyHolder.getKey().longValue());

        log.info("Отзыв {} сохранен", review);

        return Optional.of(review);

    }

    @Override
    public Optional<Review> update(Review review) {

        log.info("Обновление отзыва: {}", review);

        String sql = "UPDATE reviews SET\n" +
                "content = ?, is_positive = ?, user_id = ?, film_id = ?\n" +
                "WHERE review_id = ?";

        int countRows = jdbcTemplate.update(sql,
                review.getContent(),
                review.isPositive(),
                review.getUserId(),
                review.getFilmId(),
                review.getReviewId());

        sql = "DELETE FROM reviews WHERE review_id = ?";

        jdbcTemplate.update(sql, review.getReviewId());

        if (countRows > 0) {
            log.info("Отзыв {} обновлен", review);
            return Optional.of(review);
        }
        return Optional.empty();

    }

    @Override
    public void putLike(long reviewId, long userId) {

        log.info("Добавить отзыву {} лайк, от пользователя {}", reviewId, userId);

        deleteLike(reviewId, userId);

        int rate = 1;

        String sql = "INSERT INTO review_likes(review_id, user_id, rate) VALUES (?, ?, ?)";

        jdbcTemplate.update(sql, reviewId, userId, rate);

        log.info("Добавлен лайк отзыву {} , от пользователя {}", reviewId, userId);

    }

    @Override
    public void deleteLike(long reviewId, long userId) {

        log.info("Удалить у отзыва {} лайк / дизлайк, от пользователя {}", reviewId, userId);

        String sql = "DELETE FROM review_likes WHERE review_id = ? AND user_id = ?";

        jdbcTemplate.update(sql, reviewId, userId);

        log.info("Удален лайк у отзыва {} , от пользователя {}", reviewId, userId);

    }

    @Override
    public void putDislike(long reviewId, long userId) {

        log.info("Добавить отзыву {} лайк, от пользователя {}", reviewId, userId);

        deleteLike(reviewId, userId);

        int rate = -1;

        String sql = "INSERT INTO review_likes(review_id, user_id, rate) VALUES (?, ?, ?)";

        jdbcTemplate.update(sql, reviewId, userId, rate);

        log.info("Добавлен лайк отзыву {} , от пользователя {}", reviewId, userId);

    }

    @Override
    public void deleteDislike(long reviewId, long userId) {
        deleteLike(reviewId, userId);
    }

    @Override
    public List<Film> findTopReviews(int countReviews) {
        return null;
    }

    @Override
    public void delete(Review review) {

    }
}
