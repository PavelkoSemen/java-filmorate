package ru.yandex.practicum.filmorate.service.reviewservice;

import ru.yandex.practicum.filmorate.model.Review;

import java.util.List;

public interface ReviewService {

    boolean isValidReviewId(Long reviewId);

    boolean isValidFilmId(Long filmId);

    boolean isValidUserId(Long userId);

    boolean isValidReview(Review review);

    List<Review> getAll(Long filmId, Integer count);

    Review addReview(Review review);

    Review updateReview(Review review);

    Review getReviewBiId(Long reviewId);

    Review deleteReview(Long reviewId);

    void addLikeFromUser(Long reviewId, Long userId);

    void addDislikeFromUser(Long reviewId, Long userId);

    void removeLikeFromUser(Long reviewId, Long userId);

    void removeDislikeFromUser(Long reviewId, Long userId);
}