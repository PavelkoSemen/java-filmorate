package ru.yandex.practicum.filmorate.error;

public class SaveUserException extends RuntimeException{
    public SaveUserException() {
    }

    public SaveUserException(String message) {
        super(message);
    }

    public SaveUserException(String message, Throwable cause) {
        super(message, cause);
    }
}