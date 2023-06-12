package ru.yandex.practicum.filmorate.error;

public class UnknownMpaException extends RuntimeException {
    public UnknownMpaException() {
    }

    public UnknownMpaException(String message) {
        super(message);
    }
}