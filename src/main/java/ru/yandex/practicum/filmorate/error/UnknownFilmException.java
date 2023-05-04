package ru.yandex.practicum.filmorate.error;

public class UnknownFilmException extends RuntimeException{
    public UnknownFilmException() {
    }

    public UnknownFilmException(String message) {
        super(message);
    }

    public UnknownFilmException(String message, Throwable cause) {
        super(message, cause);
    }
}