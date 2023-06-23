package ru.yandex.practicum.filmorate.error;

public class UnknownEventException extends RuntimeException {
    public UnknownEventException() {
        super();
    }

    public UnknownEventException(String message) {
        super(message);
    }

    public UnknownEventException(String message, Throwable cause) {
        super(message, cause);
    }
}