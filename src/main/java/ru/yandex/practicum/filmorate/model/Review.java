package ru.yandex.practicum.filmorate.model;

import lombok.*;

import javax.validation.constraints.NotNull;

@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode
@ToString
public class Review {

    @Getter
    @Setter
    private Long reviewId;
    @Getter
    @Setter
    @NotNull
    private String content;
    @NotNull
    private Boolean isPositive;
    @Getter
    @Setter
    @NotNull
    private Long userId;
    @Getter
    @Setter
    @NotNull
    private Long filmId;
    @Getter
    @Setter
    private Integer useful = 0;

    public Review(Long reviewId, String content, Boolean isPositive, Long userId, Long filmId) {
        this.reviewId = reviewId;
        this.content = content;
        this.isPositive = isPositive;
        this.userId = userId;
        this.filmId = filmId;
    }

    public boolean getIsPositive() {
        return isPositive;
    }

    public void setIsPositive(boolean isPositive) {
        this.isPositive = isPositive;
    }
}
