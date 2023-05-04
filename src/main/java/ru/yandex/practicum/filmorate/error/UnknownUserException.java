package ru.yandex.practicum.filmorate.error;

public class UnknownUserException extends RuntimeException{
    public UnknownUserException() {
    }

    public UnknownUserException(String message) {
        super(message);
    }

    public UnknownUserException(String message, Throwable cause) {
        super(message, cause);
    }
}