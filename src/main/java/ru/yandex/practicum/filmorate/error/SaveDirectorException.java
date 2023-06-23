package ru.yandex.practicum.filmorate.error;

public class SaveDirectorException extends RuntimeException {

    public SaveDirectorException() {
    }

    public SaveDirectorException(String message) {
        super(message);
    }

    public SaveDirectorException(String message, Throwable cause) {
        super(message, cause);
    }
}