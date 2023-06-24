package ru.yandex.practicum.filmorate.error;

public class EntitySaveException extends RuntimeException {
    public EntitySaveException() {
    }

    public EntitySaveException(String message) {
        super(message);
    }

    public EntitySaveException(String message, Throwable cause) {
        super(message, cause);
    }
}