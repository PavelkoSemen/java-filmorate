package ru.yandex.practicum.filmorate.service.reviewservice;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.dao.ReviewRepository;
import ru.yandex.practicum.filmorate.error.UnknownReviewException;
import ru.yandex.practicum.filmorate.model.Review;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class ReviewService {

    private final ReviewRepository reviewRepository;

    @Autowired
    public ReviewService(ReviewRepository reviewRepository) {
        this.reviewRepository = reviewRepository;
    }

    /*
        Проверяем наличие объектов в базе
     */
    public boolean isValidReviewId(Long reviewId) {

        return reviewRepository.containsKey(reviewId);
    }

    public boolean isValidFilmId(Long filmId) {

        return reviewRepository.containsFilm(filmId);
    }

    public boolean isValidUserId(Long userId) {

        return reviewRepository.containsUser(userId);
    }

    public boolean isValidReview(Review review) {

        return isValidFilmId(review.getFilmId()) && isValidUserId(review.getUserId());
    }

    /*
        Методы для работы с базой отзывов
     */
    public List<Review> getAll(Long filmId, Integer count) {

        return reviewRepository.getAll(filmId, count).stream()
                .sorted((r0, r1) -> {
                    int comp = r1.getUseful().compareTo(r0.getUseful()); //сортировка по useful DESC
                    if (comp == 0) {
                        comp = r0.getReviewId().compareTo(r1.getReviewId()); //сортировка по reviewId ASC
                    }
                    return comp;
                })
                .collect(Collectors.toList());
    }

    public Review addReview(Review review) {

        if (isValidReview(review)) {
            return reviewRepository.save(review);
        } else {
            throw new UnknownReviewException("Некорректные параметры запроса: " + review);
        }
    }

    public Review updateReview(Review review) {

        if (isValidReviewId(review.getReviewId()) && isValidReview(review)) {
            return reviewRepository.update(review);
        } else {
            throw new UnknownReviewException("Некорректные параметры запроса: " + review);
        }

    }

    public Review getReviewBiId(Long reviewId) {

        if (isValidReviewId(reviewId)) {
            return reviewRepository.get(reviewId);
        } else {
            throw new UnknownReviewException("Некорректные параметры запроса: " + reviewId);
        }
    }

    public void deleteReview(Long reviewId) {

        if (isValidReviewId(reviewId)) {
            reviewRepository.delete(reviewId);
        } else {
            throw new UnknownReviewException("Некорректные параметры запроса: " + reviewId);
        }
    }

    /*
        Методы для работы с оценками отзывов
     */
    public void addLikeFromUser(Long reviewId, Long userId) {

        if (isValidReviewId(reviewId) && isValidUserId(userId)) {
            reviewRepository.putLike(reviewId, userId);
        } else {
            throw new UnknownReviewException("Некорректные параметры запроса: " + reviewId + ", " + userId);
        }
    }

    public void addDislikeFromUser(Long reviewId, Long userId) {

        if (isValidReviewId(reviewId) && isValidUserId(userId)) {
            reviewRepository.putDislike(reviewId, userId);
        } else {
            throw new UnknownReviewException("Некорректные параметры запроса: " + reviewId + ", " + userId);
        }
    }

    public void removeLikeFromUser(Long reviewId, Long userId) {

        if (isValidReviewId(reviewId) && isValidUserId(userId)) {
            reviewRepository.deleteLike(reviewId, userId);
        } else {
            throw new UnknownReviewException("Некорректные параметры запроса: " + reviewId + ", " + userId);
        }
    }

    public void removeDislikeFromUser(Long reviewId, Long userId) {

        if (isValidReviewId(reviewId) && isValidUserId(userId)) {
            reviewRepository.deleteDislike(reviewId, userId);
        } else {
            throw new UnknownReviewException("Некорректные параметры запроса: " + reviewId + ", " + userId);
        }
    }
}
