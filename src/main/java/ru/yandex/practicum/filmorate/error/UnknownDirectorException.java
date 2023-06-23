package ru.yandex.practicum.filmorate.error;

public class UnknownDirectorException extends RuntimeException {

    public UnknownDirectorException() {
    }

    public UnknownDirectorException(String message) {
        super(message);
    }

    public UnknownDirectorException(String message, Throwable cause) {
        super(message, cause);
    }
}