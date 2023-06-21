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
        if (!reviewRepository.containsKey(reviewId)) {
            throw new UnknownReviewException("Отзыв не найден: " + reviewId);
        } else {
            return true;
        }
    }

    public boolean isValidFilmId(Long filmId) {

        if (!reviewRepository.containsFilm(filmId)) {
            throw new UnknownReviewException("Фильм не найден: " + filmId);
        }
        return true;
    }

    public boolean isValidUserId(Long userId) {

        if (!reviewRepository.containsUser(userId) || (userId < 0)) {
            throw new UnknownReviewException("Юзер не найден: " + userId);
        }
        return true;
    }

    public boolean isValidReview(Review review) {

        Long filmId = review.getFilmId();
        Long userId = review.getUserId();

        return isValidFilmId(filmId) && isValidUserId(userId);
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
        } else return null;
    }

    public Review updateReview(Review review) {

        if (isValidReviewId(review.getReviewId()) && isValidReview(review)) {
            return reviewRepository.update(review);
        } else return null;

    }

    public Review getReviewBiId(Long reviewId) {

        if (isValidReviewId(reviewId)) {
            return reviewRepository.get(reviewId);
        } else {
            return null;
        }
    }

    public void deleteReview(Long reviewId) {

        if (isValidReviewId(reviewId)) {
            reviewRepository.delete(reviewId);
        }
    }

    /*
        Методы для работы с оценками отзывов
     */
    public void addLikeFromUser(Long reviewId, Long userId) {

        if (isValidReviewId(reviewId) && isValidUserId(userId)) {
            reviewRepository.putLike(reviewId, userId);
        }
    }

    public void addDislikeFromUser(Long reviewId, Long userId) {

        if (isValidReviewId(reviewId) && isValidUserId(userId)) {
            reviewRepository.putDislike(reviewId, userId);
        }
    }

    public void removeLikeFromUser(Long reviewId, Long userId) {

        if (isValidReviewId(reviewId) && isValidUserId(userId)) {
            reviewRepository.deleteLike(reviewId, userId);
        }
    }

    public void removeDislikeFromUser(Long reviewId, Long userId) {

        if (isValidReviewId(reviewId) && isValidUserId(userId)) {
            reviewRepository.deleteDislike(reviewId, userId);
        }
    }
}
