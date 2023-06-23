package ru.yandex.practicum.filmorate.error;

public class SaveEventException extends RuntimeException {
    public SaveEventException() {
        super();
    }

    public SaveEventException(String message) {
        super(message);
    }

    public SaveEventException(String message, Throwable cause) {
        super(message, cause);
    }
}