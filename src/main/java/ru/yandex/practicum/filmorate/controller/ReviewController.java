package ru.yandex.practicum.filmorate.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.model.Review;
import ru.yandex.practicum.filmorate.service.reviewservice.ReviewService;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/reviews")
public class ReviewController {

    private final ReviewService reviewService;

    @Autowired
    public ReviewController(ReviewService reviewService) {
        this.reviewService = reviewService;
    }

    /*
        Эндпоинты добавления, обновления и удаления отзывов
     */
    @PostMapping
    public Review addReview(@Valid @RequestBody Review review) {

        return reviewService.addReview(review);
    }

    @PutMapping
    public Review updateReview(@Valid @RequestBody Review review) {

        return reviewService.updateReview(review);
    }

    @DeleteMapping("{reviewId}")
    public void deleteReview(@PathVariable("reviewId") long reviewId) {

        reviewService.deleteReview(reviewId);
    }

    /*
         Эндпоинты получения
    */
    @GetMapping("{reviewId}")
    public Review getReviewById(@PathVariable("reviewId") long reviewId) {

        return reviewService.getReviewBiId(reviewId);
    }

    @GetMapping
    public List<Review> getAllReviews(
            @RequestParam(defaultValue = "-1") final Long filmId,
            @RequestParam(defaultValue = "10") final Integer count) {

        return new ArrayList<>(reviewService.getAll(filmId, count));
    }

    /*
        Эндпоинты работы с рейтингом
     */
    @PutMapping("{reviewId}/like/{userId}")
    public void addLikeFromUserToReview(
            @PathVariable("reviewId") long reviewId,
            @PathVariable("userId") long userId) {

        reviewService.addLikeFromUser(reviewId, userId);
    }

    @PutMapping("{reviewId}/dislike/{userId}")
    public void addDislikeFromUserToReview(
            @PathVariable("reviewId") long reviewId,
            @PathVariable("userId") long userId) {

        reviewService.addDislikeFromUser(reviewId, userId);
    }

    @DeleteMapping("{reviewId}/like/{userId}")
    public void removeLikeFromUserToReview(
            @PathVariable("reviewId") long reviewId,
            @PathVariable("userId") long userId) {

        reviewService.removeLikeFromUser(reviewId, userId);
    }

    @DeleteMapping("{reviewId}/dislike/{userId}")
    public void removeDislikeFromUserToReview(
            @PathVariable("reviewId") long reviewId,
            @PathVariable("userId") long userId) {

        reviewService.removeDislikeFromUser(reviewId, userId);
    }


}
