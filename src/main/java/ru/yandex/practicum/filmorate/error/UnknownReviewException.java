package ru.yandex.practicum.filmorate.error;

public class UnknownReviewException extends RuntimeException {

    public UnknownReviewException() {
    }

    public UnknownReviewException(String message) {
        super(message);
    }

    public UnknownReviewException(String message, Throwable cause) {
        super(message, cause);
    }
}