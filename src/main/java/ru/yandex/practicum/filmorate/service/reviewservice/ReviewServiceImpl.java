package ru.yandex.practicum.filmorate.service.reviewservice;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.dao.ReviewRepository;
import ru.yandex.practicum.filmorate.error.EntityNotFoundException;
import ru.yandex.practicum.filmorate.model.Review;
import ru.yandex.practicum.filmorate.model.eventenum.EventOperation;
import ru.yandex.practicum.filmorate.model.eventenum.EventType;
import ru.yandex.practicum.filmorate.utils.customannotations.EventFeed;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class ReviewServiceImpl implements ReviewService {

    private final ReviewRepository reviewRepository;

    @Autowired
    public ReviewServiceImpl(ReviewRepository reviewRepository) {
        this.reviewRepository = reviewRepository;
    }

    /*
        Проверяем наличие объектов в базе
     */
    @Override
    public boolean isValidReviewId(Long reviewId) {

        return reviewRepository.containsKey(reviewId);
    }

    @Override
    public boolean isValidFilmId(Long filmId) {

        return reviewRepository.containsFilm(filmId);
    }

    @Override
    public boolean isValidUserId(Long userId) {

        return reviewRepository.containsUser(userId);
    }

    @Override
    public boolean isValidReview(Review review) {

        return isValidFilmId(review.getFilmId()) && isValidUserId(review.getUserId());
    }

    /*
        Методы для работы с базой отзывов
     */
    @Override
    public List<Review> getAll(Long filmId, Integer count) {

        return reviewRepository.findAll(filmId, count).stream()
                .sorted((r0, r1) -> {
                    int comp = r1.getUseful().compareTo(r0.getUseful()); //сортировка по useful DESC
                    if (comp == 0) {
                        comp = r0.getReviewId().compareTo(r1.getReviewId()); //сортировка по reviewId ASC
                    }
                    return comp;
                })
                .collect(Collectors.toList());
    }

    @Override
    @EventFeed(operation = EventOperation.ADD, type = EventType.REVIEW)
    public Review addReview(Review review) {

        if (isValidReview(review)) {
            return reviewRepository.save(review);
        } else {
            throw new EntityNotFoundException("Некорректные параметры запроса: " + review);
        }
    }

    @Override
    @EventFeed(operation = EventOperation.UPDATE, type = EventType.REVIEW)
    public Review updateReview(Review review) {

        if (isValidReviewId(review.getReviewId()) && isValidReview(review)) {
            return reviewRepository.update(review);
        } else {
            throw new EntityNotFoundException("Некорректные параметры запроса: " + review);
        }

    }

    @Override
    @EventFeed(operation = EventOperation.REMOVE, type = EventType.REVIEW)
    public Review deleteReview(Long reviewId) {
        Review review = reviewRepository.findReviewById(reviewId);
        if (review != null) {
            reviewRepository.delete(reviewId);
        } else {
            throw new EntityNotFoundException("Некорректные параметры запроса: " + reviewId);
        }
        return review;
    }

    @Override
    public Review getReviewBiId(Long reviewId) {

        if (isValidReviewId(reviewId)) {
            return reviewRepository.findReviewById(reviewId);
        } else {
            throw new EntityNotFoundException("Некорректные параметры запроса: " + reviewId);
        }
    }


    /*
        Методы для работы с оценками отзывов
     */

    @Override
    public void addLikeFromUser(Long reviewId, Long userId) {

        if (isValidReviewId(reviewId) && isValidUserId(userId)) {
            reviewRepository.putLike(reviewId, userId);
        } else {
            throw new EntityNotFoundException("Некорректные параметры запроса: " + reviewId + ", " + userId);
        }
    }

    @Override
    public void addDislikeFromUser(Long reviewId, Long userId) {
        if (isValidReviewId(reviewId) && isValidUserId(userId)) {
            reviewRepository.putDislike(reviewId, userId);
        } else {
            throw new EntityNotFoundException("Некорректные параметры запроса: " + reviewId + ", " + userId);
        }
    }

    @Override
    public void removeLikeFromUser(Long reviewId, Long userId) {
        if (isValidReviewId(reviewId) && isValidUserId(userId)) {
            reviewRepository.deleteLike(reviewId, userId);
        } else {
            throw new EntityNotFoundException("Некорректные параметры запроса: " + reviewId + ", " + userId);
        }
    }

    @Override
    public void removeDislikeFromUser(Long reviewId, Long userId) {
        if (isValidReviewId(reviewId) && isValidUserId(userId)) {
            reviewRepository.deleteDislike(reviewId, userId);
        } else {
            throw new EntityNotFoundException("Некорректные параметры запроса: " + reviewId + ", " + userId);
        }
    }
}