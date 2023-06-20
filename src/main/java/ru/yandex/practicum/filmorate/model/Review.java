package ru.yandex.practicum.filmorate.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Review {

    private long reviewId;
    private String content;
    private boolean isPositive;
    private long userId;
    private long filmId;
    private Integer useful;


    public Review(long reviewId, String content, boolean isPositive, Integer userId, Integer filmId) {
        this.reviewId = reviewId;
        this.content = content;
        this.isPositive = isPositive;
        this.userId = userId;
        this.filmId = filmId;
    }
}
