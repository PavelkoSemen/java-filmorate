package ru.yandex.practicum.filmorate.dao;

import ru.yandex.practicum.filmorate.model.Review;

import java.util.List;

public interface ReviewRepository {

    List<Review> getAll(Long filmId, Integer count);

    Review get(Long reviewId);

    Review save(Review review);

    Review update(Review review);

    void delete(Long reviewId);

    void putLike(Long reviewId, Long userId);

    void deleteLike(Long reviewId, Long userId);

    void putDislike(Long reviewId, Long userId);

    void deleteDislike(Long reviewId, Long userId);

    boolean containsKey(Long reviewId);

    boolean containsFilm(Long filmId);

    boolean containsUser(Long userId);
}
