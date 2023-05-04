package ru.yandex.practicum.filmorate.error;

public class SaveFilmException extends RuntimeException {
    public SaveFilmException() {
    }

    public SaveFilmException(String message) {
        super(message);
    }

    public SaveFilmException(String message, Throwable cause) {
        super(message, cause);
    }
}