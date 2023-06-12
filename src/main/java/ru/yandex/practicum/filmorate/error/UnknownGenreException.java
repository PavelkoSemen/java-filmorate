package ru.yandex.practicum.filmorate.error;

public class UnknownGenreException extends RuntimeException {
    public UnknownGenreException() {
    }

    public UnknownGenreException(String message) {
        super(message);
    }
}