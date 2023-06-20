package ru.yandex.practicum.filmorate.dao;

import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Review;

import java.util.List;
import java.util.Optional;

public interface ReviewRepository {

    List<Review> getAll();

    Optional<Review> get(long reviewId);

    Optional<Review> save(Review review);

    Optional<Review> update(Review review);

    void putLike(long reviewId, long userId);

    void deleteLike(long reviewId, long userId);

    void putDislike(long reviewId, long userId);

    void deleteDislike(long reviewId, long userId);

    List<Film> findTopReviews(int countReviews);

    void delete(Review review);

}
